package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Token;
import org.newtonproject.newtoncore.android.data.entity.common.TokenInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.service.TokenExplorerClientType;

import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.web3j.abi.FunctionEncoder;
import org.newtonproject.web3j.abi.FunctionReturnDecoder;
import org.newtonproject.web3j.abi.TypeReference;
import org.newtonproject.web3j.abi.datatypes.Address;
import org.newtonproject.web3j.abi.datatypes.Bool;
import org.newtonproject.web3j.abi.datatypes.Function;
import org.newtonproject.web3j.abi.datatypes.Type;
import org.newtonproject.web3j.abi.datatypes.generated.Uint256;
import org.newtonproject.web3j.protocol.Web3j;
import org.newtonproject.web3j.protocol.core.DefaultBlockParameterName;
import org.newtonproject.web3j.protocol.core.methods.request.Transaction;
import org.newtonproject.web3j.protocol.core.methods.response.EthCall;
import org.newtonproject.web3j.protocol.http.HttpService;
import org.newtonproject.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TokenRepository implements TokenRepositoryType {

    private final TokenExplorerClientType tokenNetworkService;
    private final TokenLocalSource tokenLocalSource;
    private final OkHttpClient httpClient;
    private final NetworkRepositoryType ethereumNetworkRepository;
    private Web3j web3j;

    public TokenRepository(
            OkHttpClient okHttpClient,
            NetworkRepositoryType ethereumNetworkRepository,
            TokenExplorerClientType tokenNetworkService,
            TokenLocalSource tokenLocalSource) {
        this.httpClient = okHttpClient;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.tokenNetworkService = tokenNetworkService;
        this.tokenLocalSource = tokenLocalSource;
        this.ethereumNetworkRepository.addOnChangeDefaultNetwork(this::buildWeb3jClient);
        buildWeb3jClient(ethereumNetworkRepository.getDefaultNetwork());
    }

    private void buildWeb3jClient(NetworkInfo defaultNetwork) {
        web3j = Web3j.build(new HttpService(defaultNetwork.rpcServerUrl, httpClient, false));
    }

    @Override
    public Observable<Token[]> fetch(String walletAddress) {
        return Observable.create(e -> {
            NetworkInfo defaultNetwork = ethereumNetworkRepository.getDefaultNetwork();
            Wallet wallet = new Wallet(walletAddress);
            Token[] tokens = tokenLocalSource.fetch(defaultNetwork, wallet)
                    .map(items -> {
                        int len = items.length;
                        Token[] result = new Token[len];
                        for (int i = 0; i < len; i++) {
                            result[i] = new Token(items[i], null);
                        }
                        return result;
                    })
                    .blockingGet();
            e.onNext(tokens);

            updateTokenInfoCache(defaultNetwork, wallet);
            tokens = tokenLocalSource.fetch(defaultNetwork, wallet)
                        .map(items -> {
                            int len = items.length;
                            Token[] result = new Token[len];
                            for (int i = 0; i < len; i++) {
                                BigDecimal balance = null;
                                try {
                                    balance = getBalance(wallet, items[i]);
                                } catch (Exception e1) {
                                    /* Quietly */
                                }
                                result[i] = new Token(items[i], balance);
                            }
                            return result;
                        }).blockingGet();
            e.onNext(tokens);
        });
    }

    @Override
    public Completable addToken(Wallet wallet, String address, String symbol, int decimals) {
        return tokenLocalSource.put(
                ethereumNetworkRepository.getDefaultNetwork(),
                wallet,
                new TokenInfo(address, "", symbol, decimals));
    }

    private void updateTokenInfoCache(NetworkInfo defaultNetwork, Wallet wallet) {
        if (!defaultNetwork.isMainNetwork) {
            return;
        }
        tokenNetworkService
                .fetch(wallet.address)
                .flatMapCompletable(items -> Completable.fromAction(() -> {
                    for (TokenInfo tokenInfo : items) {
                        try {
                            tokenLocalSource.put(
                                    ethereumNetworkRepository.getDefaultNetwork(), wallet, tokenInfo)
                                    .blockingAwait();
                        } catch (Throwable t) {
                            Logger.d("TOKEN_REM", t.getMessage());
                        }
                    }
                }))
                .blockingAwait();
    }

    private BigDecimal getBalance(Wallet wallet, TokenInfo tokenInfo) throws Exception {
        Function function = balanceOf(wallet.address);
        String responseValue = callSmartContractFunction(function, tokenInfo.address, wallet);

        List<Type> response = FunctionReturnDecoder.decode(
                responseValue, function.getOutputParameters());
        if (response.size() == 1) {
            return new BigDecimal(((Uint256) response.get(0)).getValue());
        } else {
            return null;
        }
    }

    private static Function balanceOf(String owner) {
        return new Function(
                "balanceOf",
                Collections.singletonList(new Address(owner)),
                Collections.singletonList(new TypeReference<Uint256>() {}));
    }

    private String callSmartContractFunction(
            Function function, String contractAddress, Wallet wallet) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);

        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(wallet.address, contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        return response.getValue();
    }

    public static byte[] createTokenTransferData(String to, BigInteger tokenAmount) {
        List<Type> params = Arrays.asList(new Address(to), new Uint256(tokenAmount));

        List<TypeReference<?>> returnTypes = Arrays.asList(new TypeReference<Bool>() {
        });

        Function function = new Function("transfer", params, returnTypes);
        String encodedFunction = FunctionEncoder.encode(function);
        return Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(encodedFunction));
    }
}

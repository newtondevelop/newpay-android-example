package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.entity.common.ServiceException;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.service.AccountKeystoreService;
import org.newtonproject.newtoncore.android.data.service.BlockExplorerClientType;
import org.newtonproject.web3j.crypto.Sign;
import org.newtonproject.web3j.protocol.Web3j;
import org.newtonproject.web3j.protocol.core.DefaultBlockParameterName;
import org.newtonproject.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.newtonproject.web3j.protocol.core.methods.response.EthSendTransaction;
import org.newtonproject.web3j.protocol.http.HttpService;
import org.newtonproject.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionRepository implements TransactionRepositoryType {

	private final NetworkRepositoryType networkRepository;
	private final AccountKeystoreService accountKeystoreService;
	private final TransactionLocalSource inMemoryCache;
	private final TransactionLocalSource inDiskCache;
	private final BlockExplorerClientType blockExplorerClient;

	public TransactionRepository(
			NetworkRepositoryType networkRepository,
			AccountKeystoreService accountKeystoreService,
			TransactionLocalSource inMemoryCache,
			TransactionLocalSource inDiskCache,
			BlockExplorerClientType blockExplorerClient) {
		this.networkRepository = networkRepository;
		this.accountKeystoreService = accountKeystoreService;
		this.blockExplorerClient = blockExplorerClient;
		this.inMemoryCache = inMemoryCache;
		this.inDiskCache = inDiskCache;
		this.networkRepository.addOnChangeDefaultNetwork(this::onNetworkChanged);
	}

    @Override
	public Observable<TransactionResponse> fetchTransaction(Wallet wallet, int pageNum, boolean forceUpdate) {
		return blockExplorerClient.fetchTransactions(wallet.address, pageNum, forceUpdate);
    }

	@Override
	public Observable<Transaction> findTransaction(Wallet wallet, String txid) {
		return blockExplorerClient.findTransaction(txid);

	}

	@Override
	public Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password) {
		final Web3j web3j = Web3j.build(new HttpService(networkRepository.getDefaultNetwork().rpcServerUrl));
		return Single.fromCallable(() -> {
			EthGetTransactionCount ethGetTransactionCount = web3j
					.ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST)
					.send();
			return ethGetTransactionCount.getTransactionCount();
		})
		.flatMap(nonce -> accountKeystoreService.signTransaction(from, password, toAddress, subunitAmount, gasPrice, gasLimit, nonce.longValue(), data, networkRepository.getDefaultNetwork().chainId))
		.flatMap(signedMessage -> Single.fromCallable( () -> {
			EthSendTransaction raw = web3j
					.ethSendRawTransaction(Numeric.toHexString(signedMessage))
					.send();
			if (raw.hasError()) {
				throw new ServiceException(raw.getError().getMessage());
			}
			return raw.getTransactionHash();
		})).subscribeOn(Schedulers.io());
	}

	@Override
	public Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password, byte[] salt, byte[] iv, byte[] ciphertext) {
		final Web3j web3j = Web3j.build(new HttpService(networkRepository.getDefaultNetwork().rpcServerUrl));
		return Single.fromCallable(() -> {
			EthGetTransactionCount ethGetTransactionCount = web3j
					.ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST)
					.send();
			return ethGetTransactionCount.getTransactionCount();
		})
				.flatMap(nonce -> accountKeystoreService.signTransaction(from, password, toAddress, subunitAmount, gasPrice, gasLimit, nonce.longValue(), data, networkRepository.getDefaultNetwork().chainId, salt, iv, ciphertext))
				.flatMap(signedMessage -> Single.fromCallable( () -> {
					EthSendTransaction raw = web3j
							.ethSendRawTransaction(Numeric.toHexString(signedMessage))
							.send();
					if (raw.hasError()) {
						throw new ServiceException(raw.getError().getMessage());
					}
					return raw.getTransactionHash();
				})).subscribeOn(Schedulers.io());
	}

	@Override
	public Single<String> getRawTransaction(Wallet from, String to, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password) {
		final Web3j web3j = Web3j.build(new HttpService(networkRepository.getDefaultNetwork().rpcServerUrl));
		return Single.fromCallable(() -> {
			EthGetTransactionCount ethGetTransactionCount = web3j
					.ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST)
					.send();
			return ethGetTransactionCount.getTransactionCount();
		}).flatMap(nonce -> accountKeystoreService.signTransaction(from, password, to, subunitAmount, gasPrice, gasLimit, nonce.longValue(), data, networkRepository.getDefaultNetwork().chainId))
				.flatMap(signMessage-> Single.fromCallable(()-> Numeric.toHexString(signMessage)))
				.subscribeOn(Schedulers.io());
	}

	@Override
	public Single<Sign.SignatureData> signMessage(String from, String password, String message) {
		return accountKeystoreService.signMessage(new Wallet(from), password, message).subscribeOn(Schedulers.io());
	}

	@Override
	public Single<Sign.SignatureData> signMessageWithKey(String key, String message) {
		return accountKeystoreService.signMessage(key, message);
	}

	@Override
	public Single<HashMap<String, String>> signMessageAndGetPublickey(String from, String password, String message) {
		return accountKeystoreService.signMessageAndGetPublickey(new Wallet(from), password, message);
	}

	@Override
	public void clearTransaction(Wallet wallet) {
		inDiskCache.clear(wallet);
		inMemoryCache.clear(wallet);
	}

	private void onNetworkChanged(NetworkInfo networkInfo) {
        inMemoryCache.clear(new Wallet(""));
    }

}

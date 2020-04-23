package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.service.AccountKeystoreService;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WalletRepository implements WalletRepositoryType {

    private final PreferenceRepositoryType preferenceRepositoryType;
    private final AccountKeystoreService accountKeystoreService;
    private final NetworkRepositoryType networkRepository;
    private final OkHttpClient httpClient;
    private final String TAG = "WalletRepository";

    public WalletRepository(
            OkHttpClient okHttpClient,
            PreferenceRepositoryType preferenceRepositoryType,
            AccountKeystoreService accountKeystoreService,
            NetworkRepositoryType networkRepository) {
        this.httpClient = okHttpClient;
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.accountKeystoreService = accountKeystoreService;
        this.networkRepository = networkRepository;
    }

    @Override
    public Single<Wallet[]> fetchWallets() {
        return accountKeystoreService.fetchAccounts();
    }

    @Override
    public Single<Wallet> findWallet(String address) {
        return fetchWallets()
                .flatMap(accounts -> {
                    for (Wallet wallet : accounts) {
                        if (wallet.sameAddress(address)) {
                            return Single.just(wallet);
                        }
                    }
                    return null;
                });
    }

    @Override
    public Single<Wallet> createWallet(String password) {
        return accountKeystoreService
                .createAccount(password);
    }

    @Override
    public Single<ArrayList<String>> createWalletMnemonic() {
        return accountKeystoreService.createAccountMnemonic().subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Wallet> importKeystoreToWallet(String store, String name, String password) {
        return accountKeystoreService.importKeystore(store, password).doOnSuccess(w -> {
            if (null != name) {
                preferenceRepositoryType.setWalletName(w.address, name);
            }
        });
    }

    @Override
    public Single<Wallet> importPrivateKeyToWallet(String privateKey, String name, String newPassword) {
        return accountKeystoreService.importPrivateKey(privateKey, newPassword).doOnSuccess(w -> {
            if (null != name) {
                preferenceRepositoryType.setWalletName(w.address, name);
            }
        });
    }

    @Override
    public Single<Wallet> importMnemonicToWallet(List<String> mnemonics, String name, String newPassword) {
        return accountKeystoreService.importMnemonic(mnemonics, newPassword).doOnSuccess(w -> {
            if (null != name) {
                preferenceRepositoryType.setWalletName(w.address, name);
            }
        });
    }

    @Override
    public Single<String> exportWallet(Wallet wallet, String password) {
        return accountKeystoreService.exportAccount(wallet, password);
    }

    @Override
    public Single<String> exportWalletPrivateKey(Wallet wallet, String password) {
        return accountKeystoreService.exportPrivateKey(wallet, password);
    }

    @Override
    public Single<ArrayList<String>> exportWalletMnemonic(Wallet wallet, String password) {
        return accountKeystoreService.exportMnemonic(wallet, password);
    }

    @Override
    public Completable deleteWallet(String address, String password) {
        return accountKeystoreService.deleteAccount(address, password);
    }

    @Override
    public Completable updateWallet(String address, String password, String newPassword) {
        return accountKeystoreService.updateAccount(address, password, newPassword);
    }

    @Override
    public Completable setDefaultWallet(Wallet wallet) {
        return Completable.fromAction(() -> preferenceRepositoryType.setCurrentWalletAddress(wallet == null ? null : wallet.address));
    }

    @Override
    public Single<Wallet> getDefaultWallet() {
        return Single.fromCallable(preferenceRepositoryType::getCurrentWalletAddress)
                .flatMap(this::findWallet);
    }

    @Override
    public Single<BigInteger> balanceInWei(Wallet wallet) {
        return Single.fromCallable(() -> Web3jFactory
                .build(new HttpService(networkRepository.getDefaultNetwork().rpcServerUrl, httpClient, false))
                .ethGetBalance(NewAddressUtils.newAddress2HexAddress(wallet.address), DefaultBlockParameterName.LATEST)
                .send()
                .getBalance())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<BigInteger> getGasPrice() {
        return Single.fromCallable(() -> Web3jFactory
                .build(new HttpService(networkRepository.getDefaultNetwork().rpcServerUrl, httpClient, false))
                .ethGasPrice()
                .send()
                .getGasPrice())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<BigInteger> estimateGas(org.web3j.protocol.core.methods.request.Transaction transaction) {
        return Single.fromCallable(() -> Web3jFactory
                .build(new HttpService(networkRepository.getDefaultNetwork().rpcServerUrl, httpClient, false))
                .ethEstimateGas(transaction)
                .send()
                .getAmountUsed())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<String> getWalletType(String address) {
        return accountKeystoreService.getWalletType(address);
    }

    @Override
    public Completable checkKeystore(String keystore, String password) {
        return accountKeystoreService.checkKeystore(keystore, password);
    }

    @Override
    public Single<Wallet> importKeystore(String keystore, String password, String newPassword) {
        return accountKeystoreService.importKeystore(keystore, password, newPassword);
    }

}

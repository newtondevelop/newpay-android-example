package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface WalletRepositoryType {
    Single<Wallet[]> fetchWallets();

    Single<Wallet> findWallet(String address);

    Single<Wallet> createWallet(String password);

    Single<ArrayList<String>> createWalletMnemonic();

    Single<Wallet> importKeystoreToWallet(String store, String name, String password);

    Single<Wallet> importPrivateKeyToWallet(String privateKey, String name, String password);

    Single<Wallet> importMnemonicToWallet(List<String> mnemonics, String name, String password);

    Single<String> exportWallet(Wallet wallet, String password);

    Single<String> exportWalletPrivateKey(Wallet wallet, String password);

    Single<ArrayList<String>> exportWalletMnemonic(Wallet wallet, String password);

    Completable deleteWallet(String address, String password);

    Completable updateWallet(String address, String password, String newPassword);

    Completable setDefaultWallet(Wallet wallet);

    Single<Wallet> getDefaultWallet();

    Single<BigInteger> balanceInWei(Wallet wallet);

    Single<BigInteger> getGasPrice();

    Single<BigInteger> estimateGas(Transaction transaction);

    Single<String> getWalletType(String address);

    Completable checkKeystore(String keystore, String password);

    Single<Wallet> importKeystore(String keystore, String password, String newPassword);

}

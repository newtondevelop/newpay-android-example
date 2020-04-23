package org.newtonproject.newtoncore.android.data.interact;


import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.PasswordStore;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.web3j.crypto.Sign;

import java.math.BigInteger;
import java.util.HashMap;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class CreateTransactionInteract {
    private final TransactionRepositoryType transactionRepository;
    private PasswordStore passwordStore;

    public CreateTransactionInteract(TransactionRepositoryType transactionRepository, PasswordStore passwordStore) {
        this.transactionRepository = transactionRepository;
        this.passwordStore = passwordStore;
    }

    public CreateTransactionInteract(TransactionRepositoryType transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Single<String> create(Wallet from, String to, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String mainpassword) {
        return Single.fromCallable(() -> mainpassword)
                .flatMap(password ->
                        transactionRepository.createTransaction(from, to, subunitAmount, gasPrice, gasLimit, data, password)
                .observeOn(AndroidSchedulers.mainThread()));
    }

    public Single<String> create(Wallet from, String to, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String mainpassword, byte[] salt, byte[] iv, byte[] ciphertext) {
        return Single.fromCallable(() -> mainpassword)
                .flatMap(password ->
                        transactionRepository.createTransaction(from, to, subunitAmount, gasPrice, gasLimit, data, password, salt, iv, ciphertext)
                                .observeOn(AndroidSchedulers.mainThread()));
    }

    public Single<String> getRawTransaction(Wallet from, String to, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password) {
        return Single.fromCallable(()-> password)
                .flatMap(mainPassword->
                        transactionRepository.getRawTransaction(from, to, subunitAmount, gasPrice, gasLimit, data, password)
                ).observeOn(AndroidSchedulers.mainThread());
    }


    public Single<Sign.SignatureData> signMessage(String from, String password, String message) {
        return transactionRepository.signMessage(from, password, message)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<HashMap<String, String>> signMessageAndGetPublicKey(String from, String password, String message) {
        return transactionRepository.signMessageAndGetPublickey(from, password, message)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Sign.SignatureData> signMessageWithKey(String key, String message) {
        return transactionRepository.signMessageWithKey(key, message)
                .observeOn(AndroidSchedulers.mainThread());
    }



}

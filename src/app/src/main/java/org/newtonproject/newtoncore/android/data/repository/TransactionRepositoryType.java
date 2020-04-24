package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.web3j.crypto.Sign;

import java.math.BigInteger;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface TransactionRepositoryType {
	Observable<TransactionResponse> fetchTransaction(Wallet wallet, int pageNum, boolean forceUpdate);
	Observable<Transaction> findTransaction(Wallet wallet, String txid);

	Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password);
	// which transaction from nfc
	Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password, byte[] salt, byte[] iv, byte[] ciphertext);
	Single<Sign.SignatureData> signMessage(String from, String password, String message);
	Single<Sign.SignatureData> signMessageWithKey(String key, String message);
	// signmessage and get publickey
	Single<HashMap<String, String>> signMessageAndGetPublickey(String from, String password, String message);
	void clearTransaction(Wallet wallet);

    Single<String> getRawTransaction(Wallet from, String to, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password);
}

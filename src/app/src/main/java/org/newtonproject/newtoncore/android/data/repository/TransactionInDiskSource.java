package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionContract;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionOperation;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.entity.RealmTransactionContract;
import org.newtonproject.newtoncore.android.data.repository.entity.RealmTransactionInfo;
import org.newtonproject.newtoncore.android.data.repository.entity.RealmTransactionOperation;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;

import java.util.ArrayList;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionInDiskSource implements TransactionLocalSource {
	private static final String TAG = TransactionInDiskSource.class.getSimpleName();
	private TransactionLocalSource inMemeryCache = new TransactionInMemorySource();

	private Realm getRealmInstance(Wallet wallet) {
		RealmConfiguration config = new RealmConfiguration.Builder()
				.name(NewAddressUtils.hexAddress2NewAddress(wallet.address) + "-" + C.Key.TRANSACTION + ".realm")
				.schemaVersion(C.DATABASE_VERSION)
				.deleteRealmIfMigrationNeeded()
				.migration(new RealmDatabaseMigration())
				.build();
		return Realm.getInstance(config);
	}

	@Override
	public Single<TransactionResponse> fetchTransactions(Wallet wallet) {
		Realm realm = null;
		ArrayList<Transaction> lists = new ArrayList<>();
		try {
			realm = getRealmInstance(wallet);
			RealmResults<RealmTransactionInfo> infos = realm.where(RealmTransactionInfo.class)
					.findAll();

			if(null != infos && infos.size() > 0) {
				for(int j = infos.size() - 1; j >= 0 ; j--) {
					RealmTransactionInfo info = infos.get(j);
					RealmList<RealmTransactionOperation> operations = info.getOperations();
					TransactionOperation transactionOperation[] = new TransactionOperation[operations.size()];
					for(int i = 0 ; i < operations.size(); i++) {
						RealmTransactionOperation operation = operations.get(i);
						if(operation != null) {
							RealmTransactionContract contract = operation.getContract();
							if(contract != null) {
								TransactionContract tContract = new TransactionContract();
								tContract.address = contract.getAddress();
								tContract.decimals = contract.getDecimals();
								tContract.name = contract.getName();
								tContract.symbol = contract.getSymbol();
								tContract.totalSupply = contract.getTotalSupply();
								transactionOperation[i].contract = tContract;
							} else {
								transactionOperation[i].contract = null;
							}
							transactionOperation[i].from = operation.getFrom();
							transactionOperation[i].to = operation.getTo();
							transactionOperation[i].transactionId = operation.getTransactionId();
							transactionOperation[i].value = operation.getValue();
							transactionOperation[i].viewType = operation.getViewType();
						}
					}
					lists.add(new Transaction(info.getHash(), info.getError(), info.getBlockNumber(), info.getTimStamp(), info.getNonce(), info.getFrom(), info.getTo(),
							info.getValue(), info.getGas(), info.getGasPrice(), info.getInput(), info.getGasUsed(), transactionOperation));
				}
				Transaction[] objects = lists.toArray(new Transaction[0]);
				return Single.fromCallable(() -> {
					TransactionResponse res = new TransactionResponse();
					res.docs = objects;
					res.total = objects.length;
					return res;
				});
			}
			return Single.fromCallable(TransactionResponse::new);
		} finally {
			if(null != realm) {
				realm.close();
			}
		}
	}

	@Override
	public void putTransactions(Wallet wallet, Transaction[] transactions) {
		Realm realm = null;
		try {
			realm = getRealmInstance(wallet);
			for (int j = transactions.length - 1; j >= 0; j--) {
				Transaction t = transactions[j];
				RealmTransactionInfo info = realm.where(RealmTransactionInfo.class)
						.equalTo("hash", t.hash)
						.findFirst();
				if(null == info) {
					realm.executeTransaction(r -> {
						RealmTransactionInfo obj = r.createObject(RealmTransactionInfo.class, t.hash);
						obj.setBlockNumber(t.blockNumber);
						obj.setError(t.error);
						obj.setFrom(t.from);
						obj.setGas(t.gas);
						obj.setGasPrice(t.gasPrice);
						obj.setGasUsed(t.gasUsed);
						obj.setInput(t.input);
						obj.setNonce(t.nonce);
						RealmTransactionOperation[] operations = new RealmTransactionOperation[t.operations.length];
						for(int i = 0; i < t.operations.length; i++) {
							RealmTransactionContract realmContract = new RealmTransactionContract();
							realmContract.setAddress(t.operations[i].contract.address);
							realmContract.setDecimals(t.operations[i].contract.decimals);
							realmContract.setName(t.operations[i].contract.name);
							realmContract.setSymbol(t.operations[i].contract.symbol);
							realmContract.setTotalSupply(t.operations[i].contract.totalSupply);
							operations[i].setFrom(t.operations[i].from);
							operations[i].setTo(t.operations[i].to);
							operations[i].setTransactionId(t.operations[i].transactionId);
							operations[i].setValue(t.operations[i].value);
							operations[i].setViewType(t.operations[i].viewType);
						}
						obj.setOperations(new RealmList<RealmTransactionOperation>(operations));
						obj.setTo(t.to);
						obj.setTimStamp(t.timeStamp);
						obj.setValue(t.value);
					});
				}
			}

		} finally {
			if(null != realm) {
				realm.close();
			}
		}
	}

    @Override
    public void clear(Wallet wallet) {
		Realm realm = null;
		try {
			realm = getRealmInstance(wallet);
			RealmResults<RealmTransactionInfo> info = realm.where(RealmTransactionInfo.class)
					.findAll();
			if(null != info) {
				realm.executeTransaction(r -> info.deleteAllFromRealm());
			}
		} finally {
			if(null != realm) {
				realm.close();
			}
		}
    }

	@Override
	public Single<Transaction> fetchTransaction(Wallet wallet, String hash) {
		Realm realm = null;
		try {
			realm = getRealmInstance(wallet);
			RealmTransactionInfo info = realm.where(RealmTransactionInfo.class)
					.equalTo("hash", hash)
					.findFirst();
			if(null != info) {
				RealmList<RealmTransactionOperation> operations = info.getOperations();
				TransactionOperation transactionOperation[] = new TransactionOperation[operations.size()];
				for(int i = 0 ; i < operations.size(); i++) {
					RealmTransactionOperation operation = operations.get(i);
					if(operation != null) {
						RealmTransactionContract contract = operation.getContract();
						if(contract != null) {
							TransactionContract tContract = new TransactionContract();
							tContract.address = contract.getAddress();
							tContract.decimals = contract.getDecimals();
							tContract.name = contract.getName();
							tContract.symbol = contract.getSymbol();
							tContract.totalSupply = contract.getTotalSupply();
							transactionOperation[i].contract = tContract;
						} else {
							transactionOperation[i].contract = null;
						}
						transactionOperation[i].from = operation.getFrom();
						transactionOperation[i].to = operation.getTo();
						transactionOperation[i].transactionId = operation.getTransactionId();
						transactionOperation[i].value = operation.getValue();
						transactionOperation[i].viewType = operation.getViewType();
					}
				}
				Transaction transaction = new Transaction(info.getHash(), info.getError(), info.getBlockNumber(), info.getTimStamp(), info.getNonce(), info.getFrom(), info.getTo(),
						info.getValue(), info.getGas(), info.getGasPrice(), info.getInput(), info.getGasUsed(), transactionOperation);
				return Single.fromCallable(() -> transaction);
			} else {
				return Single.fromCallable(() -> null);
			}
		} finally {
			if(null != realm) {
				realm.close();
			}
		}
	}
}

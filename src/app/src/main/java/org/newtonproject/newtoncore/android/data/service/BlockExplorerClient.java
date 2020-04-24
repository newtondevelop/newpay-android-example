package org.newtonproject.newtoncore.android.data.service;

import com.google.gson.Gson;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.cache.CacheProvider;
import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.entity.common.NewQueryResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictDynamicKeyGroup;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class BlockExplorerClient implements BlockExplorerClientType {

	private final OkHttpClient httpClient;
	private final Gson gson;
	private final NetworkRepositoryType networkRepository;
	private EtherScanApiClient etherScanApiClient;
	private CacheProvider cacheProvider;

	public BlockExplorerClient(
			OkHttpClient httpClient,
			Gson gson,
			NetworkRepositoryType networkRepository) {
		this.httpClient = httpClient;
		this.gson = gson;
		this.networkRepository = networkRepository;
		this.networkRepository.addOnChangeDefaultNetwork(this::onNetworkChanged);
		NetworkInfo networkInfo = networkRepository.getDefaultNetwork();
		onNetworkChanged(networkInfo);
	}

	private void buildApiClient(String baseUrl) {
		etherScanApiClient = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.client(httpClient)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build()
				.create(EtherScanApiClient.class);

		cacheProvider = new RxCache.Builder()
				.useExpiredDataIfLoaderNotAvailable(true)
				.persistence(new File(C.TRANSACTION_CACHE), new GsonSpeaker(gson))
				.using(CacheProvider.class);
	}

	@Override
	public Observable<TransactionResponse> fetchTransactions(String address, int pageNum, boolean forceUpdate) {
		if(pageNum == 1) {
			return cacheProvider.fetchTransactions(etherScanApiClient
					.fetchTransactions(address, pageNum), new DynamicKeyGroup(pageNum, address), new EvictDynamicKeyGroup(forceUpdate))
					.subscribeOn(Schedulers.io());
		} else {
			return etherScanApiClient
					.fetchTransactions(address, pageNum)
					.subscribeOn(Schedulers.io());
		}

	}

	@Override
	public Observable<Transaction> findTransaction(String txid) {
		return cacheProvider.findTransaction(etherScanApiClient
				.findTransaction(txid), new DynamicKey(txid), new EvictDynamicKey(false))
				.map(r -> r.tx)
				.subscribeOn(Schedulers.io());
	}

	private void onNetworkChanged(NetworkInfo networkInfo) {
		buildApiClient(networkInfo.explorerUrl);
	}

	private interface EtherScanApiClient {
		@GET("/api/v1/transactions?limit=" + C.LIMIT)
		Observable<TransactionResponse> fetchTransactions(@Query("address") String address, @Query("pageNum") int pageNum);

		@GET("/api/v1/transaction/")
		Observable<NewQueryResponse> findTransaction(@Query("txid") String txid);
	}
}

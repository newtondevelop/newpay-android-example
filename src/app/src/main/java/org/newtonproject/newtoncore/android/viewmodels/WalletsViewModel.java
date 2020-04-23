package org.newtonproject.newtoncore.android.viewmodels;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.cache.CacheProvider;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.CreateWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.DeleteWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.ExportWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.SetDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.manager.AccountManager;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.manager.ProfileManager;
import org.newtonproject.newtoncore.android.router.ImportWalletRouter;
import org.newtonproject.newtoncore.android.router.TransactionsRouter;
import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.newtoncore.android.views.base.CreateWalletSuccessActivity;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

import static org.newtonproject.newtoncore.android.C.REQUEST_CODE_IMPORT_WALLET;
import static org.newtonproject.newtoncore.android.utils.StringUtil.getString;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WalletsViewModel extends BaseViewModel {

    private static final String TAG = "WalletViewModel";
    private final CreateWalletInteract createWalletInteract;
	private final SetDefaultWalletInteract setDefaultWalletInteract;
	private final DeleteWalletInteract deleteWalletInteract;
	private final FetchWalletsInteract fetchWalletsInteract;
	private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final ExportWalletInteract exportWalletInteract;
    private final PreferenceRepositoryType preferenceRepositoryType;

	private final ImportWalletRouter importWalletRouter;
    private final TransactionsRouter transactionsRouter;

    private Gson gson = new Gson();
	private MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
	private MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
	private MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
	private MutableLiveData<ArrayList<String>> createWalletMnemonic = new MutableLiveData<>();
	private MutableLiveData<Throwable> createWalletError = new MutableLiveData<>();
	private MutableLiveData<String> exportedStore = new MutableLiveData<>();
	private MutableLiveData<Throwable> exportWalletError = new MutableLiveData<>();

	private MutableLiveData<ArrayList<String>> exportedMnemonicStore = new MutableLiveData<>();
	private MutableLiveData<String> exportPrivateKeyStore = new MutableLiveData<>();
	private MutableLiveData<Throwable> onDeleteError = new MutableLiveData<>();
	private MutableLiveData<String> onWalletType = new MutableLiveData<>();

    public WalletsViewModel(
			CreateWalletInteract createWalletInteract,
			SetDefaultWalletInteract setDefaultWalletInteract,
			DeleteWalletInteract deleteWalletInteract,
			FetchWalletsInteract fetchWalletsInteract,
			FindDefaultWalletInteract findDefaultWalletInteract,
			ExportWalletInteract exportWalletInteract,
			ImportWalletRouter importWalletRouter,
			TransactionsRouter transactionsRouter,
			PreferenceRepositoryType preferenceRepositoryType) {
		this.createWalletInteract = createWalletInteract;
		this.setDefaultWalletInteract = setDefaultWalletInteract;
		this.deleteWalletInteract = deleteWalletInteract;
		this.fetchWalletsInteract = fetchWalletsInteract;
		this.findDefaultWalletInteract = findDefaultWalletInteract;
		this.importWalletRouter = importWalletRouter;
		this.exportWalletInteract = exportWalletInteract;
		this.transactionsRouter = transactionsRouter;
		this.preferenceRepositoryType = preferenceRepositoryType;
	}
	public LiveData<String> onWalletType() {
		return onWalletType;
	}
	public LiveData<Wallet[]> wallets() {
		return wallets;
	}

	public LiveData<Wallet> defaultWallet() {
		return defaultWallet;
	}

    public LiveData<Wallet> createdWallet() {
        return createdWallet;
    }

    public LiveData<ArrayList<String>> createWalletMnemonic() { return createWalletMnemonic; }

    public LiveData<Throwable> exportWalletError() { return exportWalletError; }

    public LiveData<Throwable> onDeleteError() { return onDeleteError; }

    private MutableLiveData<Wallet> onCreateWallet = new MutableLiveData<>();
    public LiveData<Wallet> onCreateWallet() { return onCreateWallet; }

    public LiveData<String> exportedStore() {
        return exportedStore;
    }

    public LiveData<String> exportPrivateKeyStore() {
    	return exportPrivateKeyStore;
    }

    public LiveData<ArrayList<String>> exportedMnemonicStore() {
    	return exportedMnemonicStore;
    }

	public void setDefaultWallet(Wallet wallet) {
		disposable = setDefaultWalletInteract
				.set(wallet)
				.subscribe(()->{
					defaultWallet.postValue(wallet);
				}, error->{
					onCommonError.postValue(error);
				});

	}

	public void setInviteCode(String inviteCode) {
    	preferenceRepositoryType.setInviteCode(inviteCode);
	}

	public void deleteWallet(Wallet wallet, String password) {
    	progress.postValue(true);
		disposable = deleteWalletInteract
				.delete(wallet, password)
				.doOnEvent((a, b) -> {
					deleteHepCache();

					deleteTransactionCache();
					ProfileManager.getInstance().clear();
					AccountManager.getInstance().clear();
				})
				.subscribe(res -> {
					progress.postValue(false);
					wallets.postValue(res);
				}, error->{
					progress.postValue(false);
					onDeleteError.postValue(error);
				});
	}

	private void deleteTransactionCache() {
		RxCache transactionCache = new RxCache.Builder()
				.useExpiredDataIfLoaderNotAvailable(true)
				.persistence(new File(C.TRANSACTION_CACHE), new GsonSpeaker(gson));
		CacheProvider using1 = transactionCache.using(CacheProvider.class);
		Disposable subscribe = transactionCache.evictAll().subscribe(System.out::println, error -> Logger.e(error.getMessage()));

	}

	private void deleteHepCache() {
		RxCache hepCache = new RxCache.Builder()
				.useExpiredDataIfLoaderNotAvailable(true)
				.persistence(new File(C.HEP_CACHE), new GsonSpeaker(gson));
		CacheProvider using = hepCache.using(CacheProvider.class);
		Disposable subscribe = hepCache.evictAll().subscribe(System.out::println, error -> {
			Logger.e(error.getMessage());
		});
	}


	private void onFetchWallets(Wallet[] items) {
		progress.postValue(false);
		wallets.postValue(items);
		disposable = findDefaultWalletInteract
				.find()
				.subscribe(next->defaultWallet.postValue(next), error-> onCommonError.postValue(error));
	}

	public void fetchWallets() {
		progress.postValue(true);
		disposable = fetchWalletsInteract
				.fetch()
				.subscribe(this::onFetchWallets, this::onError);
	}

	public void newWalletMnemonic() {
    	progress.postValue(true);
		Disposable subscribe = createWalletInteract.createMnemonic()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						next-> {
							progress.postValue(false);
							createWalletMnemonic.postValue(next);
						},
						error->{
							progress.postValue(false);
							createWalletError.postValue(error);
						});
	}

	public void createWalletDirect(String password, String name) {
    	progress.postValue(true);
		Disposable subscribe = createWalletInteract
				.createWalletDirect(password, name)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						next -> {
							progress.postValue(false);
							preferenceRepositoryType.setNeedBackUp(true);
							onCreateWallet.postValue(next);
						},
						error -> {
							progress.postValue(false);
							onCommonError.postValue(error);
						}
				);
	}

	public void exportWallet(Wallet wallet, String storePassword) {
    	progress.postValue(true);
		Disposable subscribe = exportWalletInteract
				.export(wallet, storePassword)
				.subscribe(next -> {
					progress.postValue(false);
					exportedStore.postValue(next);
				}, error -> {
					progress.postValue(false);
					exportWalletError.postValue(error);
				});
	}

    public void exportWalletPrivateKey(Wallet wallet, String storePassword) {
    	progress.postValue(true);
		Disposable subscribe = exportWalletInteract
				.exportPrivateKey(wallet, storePassword)
				.subscribe(next -> {
					progress.postValue(false);
					exportPrivateKeyStore.postValue(next);
				}, error -> {
					progress.postValue(false);
					exportWalletError.postValue(error);
				});
	}

    public void exportWalletMnemonic(Wallet wallet, String storePassword) {
    	progress.postValue(true);
		Disposable subscribe = exportWalletInteract
				.exportMnemonic(wallet, storePassword)
				.subscribe(next -> {
					progress.postValue(false);
					exportedMnemonicStore.postValue(next);
				}, error -> {
					progress.postValue(false);
					exportWalletError.postValue(error);
				});
	}

	public void importWallet(Activity activity) {
		importWalletRouter.openForResult(activity, REQUEST_CODE_IMPORT_WALLET);
	}

    public void showTransactions(Context context) {
        transactionsRouter.open(context, true);
    }

	public void openConfirm(Context mContext, String address) {
		Intent intent = new Intent(mContext, CreateWalletSuccessActivity.class);
		intent.putExtra(C.EXTRA_CONFIRM_TITLE, getString(R.string.wallet_created));
		intent.putExtra(C.EXTRA_CONFIRM_CONTENT, String.format(getString(R.string.your_wallet_address), NewAddressUtils.hexAddress2NewAddress(address)));
		intent.putExtra(C.EXTRA_CONFIRM_ICON, R.mipmap.tab_wallet);
		intent.putExtra(C.EXTRA_CONFIRM_BUTTON_TEXT, getString(R.string.action_backup));
		mContext.startActivity(intent);
	}
}

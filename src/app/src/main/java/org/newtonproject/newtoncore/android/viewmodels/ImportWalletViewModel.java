package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.ImportWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.views.base.CreateWalletSuccessActivity;
import org.newtonproject.newtoncore.android.views.widget.OnImportKeystoreListener;
import org.newtonproject.newtoncore.android.views.widget.OnImportMnemonicListener;
import org.newtonproject.newtoncore.android.views.widget.OnImportPrivateKeyListener;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;

import static org.newtonproject.newtoncore.android.utils.StringUtil.getString;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ImportWalletViewModel extends BaseViewModel implements OnImportKeystoreListener, OnImportPrivateKeyListener, OnImportMnemonicListener {
    private final String TAG = "ImportWalletViewModel";

    private final ImportWalletInteract importWalletInteract;
    private final PreferenceRepositoryType preferenceRepositoryType;

    private final MutableLiveData<Wallet> onWallet = new MutableLiveData<>();
    public LiveData<Wallet> onWallet() {
        return onWallet;
    }


    private final MutableLiveData<Throwable> onImportError = new MutableLiveData<>();
    public LiveData<Throwable> onImportError() { return onImportError; }

    private MutableLiveData<Boolean> onCheckKeystore = new MutableLiveData<>();
    public LiveData<Boolean> onCheckKeyStore() { return onCheckKeystore; }


    ImportWalletViewModel(ImportWalletInteract importWalletInteract,
                          PreferenceRepositoryType preferenceRepositoryType) {
        this.importWalletInteract = importWalletInteract;
        this.preferenceRepositoryType = preferenceRepositoryType;
    }

    @Override
    public void onKeystore(String keystore, String name, String password) {
        progress.postValue(true);
        Disposable subscribe = importWalletInteract
                .importKeystore(keystore, name, password)
                .subscribe(
                        next -> {
                            progress.postValue(false);
                            preferenceRepositoryType.setWalletName(next.address, name);
                            preferenceRepositoryType.setCurrentWalletAddress(next.address);
                            onWallet.postValue(next);
                        },
                        error -> {
                            progress.postValue(false);
                            onImportError.postValue(error);
                        });

    }

    @Override
    public void onPrivateKey(String key, String name, String password) {
        progress.postValue(true);
        Disposable subscribe = importWalletInteract
                .importPrivateKey(key, name, password)
                .subscribe(
                        next -> {
                            progress.postValue(false);
                            preferenceRepositoryType.setCurrentWalletAddress(next.address);
                            preferenceRepositoryType.setWalletName(next.address, name);
                            onWallet.postValue(next);
                        },
                        error -> {
                            progress.postValue(false);
                            onImportError.postValue(error);
                        });
    }

    @Override
    public void onMnemonic(List<String> mnemonics, String name, String password) {
        progress.postValue(true);
        Disposable subscribe = importWalletInteract
                .importMnemonic(mnemonics, name, password)
                .subscribe(
                        next -> {
                            progress.postValue(false);
                            preferenceRepositoryType.setCurrentWalletAddress(next.address);
                            preferenceRepositoryType.setWalletName(next.address, name);
                            onWallet.postValue(next);
                        },
                        error -> {
                            progress.postValue(false);
                            onImportError.postValue(error);
                        });
    }

    public void checkKeystore(String keystore, String password) {
        progress.postValue(true);
        Disposable subscribe = importWalletInteract
                .checkKeystore(keystore, password)
                .subscribe(
                        () -> {
                            progress.postValue(false);
                            onCheckKeystore.postValue(true);
                        },
                        error -> {
                            progress.postValue(false);
                            onCheckKeystore.postValue(false);
                        }
                );
    }

    public void importKeysotreWithNewPassword(String name, String keystore, String oldPassword, String newpassword) {
        progress.postValue(true);
        Disposable subscribe = importWalletInteract
                .importKeystoreWithNewPassword(keystore, oldPassword, newpassword)
                .subscribe(
                        next -> {
                            progress.postValue(false);
                            preferenceRepositoryType.setCurrentWalletAddress(next.address);
                            preferenceRepositoryType.setWalletName(next.address, name);
                            onWallet.postValue(next);
                        },
                        error -> {
                            progress.postValue(false);
                            onCommonError.postValue(error);
                        }
                );
    }

    public void openConfirm(Context mContext, String address) {
        Intent intent = new Intent(mContext, CreateWalletSuccessActivity.class);
        intent.putExtra(C.EXTRA_CONFIRM_TITLE, getString(R.string.wallet_imported));
        intent.putExtra(C.EXTRA_CONFIRM_CONTENT, String.format(getString(R.string.your_wallet_address), NewAddressUtils.hexAddress2NewAddress(address)));
        intent.putExtra(C.EXTRA_CONFIRM_ICON, R.mipmap.tab_wallet);
        intent.putExtra(C.EXTRA_CONFIRM_BUTTON_TEXT, getString(R.string.text_go_to_wallet));
        mContext.startActivity(intent);
    }

    public void openConfirm(Context mContext) {
        Intent intent = new Intent(mContext, CreateWalletSuccessActivity.class);
        intent.putExtra(C.EXTRA_CONFIRM_TITLE, getString(R.string.wallet_backup_successed));
        intent.putExtra(C.EXTRA_CONFIRM_ICON, R.mipmap.tab_wallet);
        intent.putExtra(C.EXTRA_CONFIRM_BUTTON_TEXT, getString(R.string.text_go_to_wallet));
        mContext.startActivity(intent);
    }

    public void closeBackUp() {
        preferenceRepositoryType.setNeedBackUp(false);
    }
}

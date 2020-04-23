package org.newtonproject.newtoncore.android.viewmodels.addressbook;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;

import org.newtonproject.newtoncore.android.BuildConfig;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.ContactsRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.utils.FileUtils;
import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.viewmodels.BaseViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.newtonproject.newtoncore.android.utils.StringUtil.getString;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AddressBookViewModel extends BaseViewModel {

    private ContactsRepositoryType contactsRepositoryType;
    private PreferenceRepositoryType preference;
    private MutableLiveData<ArrayList<ContactsInfo>> contactInfo = new MutableLiveData<>();
    public LiveData<ArrayList<ContactsInfo>> onContactInfo() {
        return contactInfo;
    }

    private MutableLiveData<Boolean> onDeleteContact = new MutableLiveData<>();
    public LiveData<Boolean> onDeleteContact() {
        return onDeleteContact;
    }

    AddressBookViewModel(ContactsRepositoryType contactsRepositoryType,
                         PreferenceRepositoryType preference) {
        this.contactsRepositoryType = contactsRepositoryType;
        this.preference = preference;
    }

    public void getAllContacts(Wallet wallet) {
        Disposable subscribe = Observable.fromCallable(() -> contactsRepositoryType
                .getAllContacts(wallet))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            contactInfo.postValue(next);
                        },
                        error -> {
                            onCommonError.postValue(error);
                        }
                );
    }

    public void deleteContact(Wallet wallet, String address) {
        Disposable subscribe = Observable.fromCallable(() -> contactsRepositoryType
                .deleteContact(wallet, address))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            onDeleteContact.postValue(next);
                        },
                        error -> {
                            onCommonError.postValue(error);
                        }
                );
    }

    private MutableLiveData<File> onExportContact = new MutableLiveData<>();
    public LiveData<File> onExportContact() { return onExportContact; }

    public void exportContact(Wallet wallet, File saveFile) {
        progress.postValue(true);
        Disposable subscribe = Observable
                .fromCallable(() -> contactsRepositoryType.getAllContacts(wallet))
                .flatMap(infos -> {
                    ArrayList<VCard> list = new ArrayList<>();
                    for (ContactsInfo info : infos) {
                        VCard vcard = new VCard();
                        vcard.setNickname(info.name);
                        vcard.setExtendedProperty("NEWADDRESS", info.address);
                        list.add(vcard);
                    }
                    if(saveFile.exists()) {
                        saveFile.delete();
                    }
                    if(!saveFile.exists()) {
                        boolean newFile = saveFile.createNewFile();
                        if(newFile) {
                            Ezvcard.write(list).go(saveFile);
                        }else{
                            Logger.e("create contact file error");
                        }
                    }
                    return Observable.fromCallable(() -> saveFile);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            progress.postValue(false);
                            onExportContact.postValue(next);
                        },
                        error -> {
                            progress.postValue(false);
                            onCommonError.postValue(error);
                        }
                );
    }

    private MutableLiveData<Boolean> onImportContact = new MutableLiveData<>();
    public LiveData<Boolean> onImportContact() { return onImportContact; }

    public void importContact(Wallet wallet, File file) {
        progress.postValue(true);
        Logger.e("file exist:" + file.exists());
        Disposable subscribe = Observable
                .fromCallable(() -> {
                    List<VCard> all = Ezvcard.parse(file).all();
                    for (VCard vcard : all) {
                        String name = vcard.getNickname().getValues().get(0);
                        String address = vcard.getExtendedProperty("NEWADDRESS").getValue();
                        if(NewAddressUtils.checkNewAddress(address)) {
                            contactsRepositoryType.addContact(wallet, new ContactsInfo(address, name));
                        }
                    }
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            progress.postValue(false);
                            onImportContact.postValue(next);
                        },
                        error -> {
                            progress.postValue(false);
                            onCommonError.postValue(error);
                        }
                );
    }

    private MutableLiveData<String> onCurrentAddress = new MutableLiveData<>();
    public LiveData<String> onCurrentAddress() { return onCurrentAddress; }

    public void getCurrentAddress() {
        onCurrentAddress.postValue(preference.getCurrentWalletAddress());
    }

    public void openShare(Context mContext, File file) {
        if(file.exists()) {
            checkFileUriExposure();
            Uri uri;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(mContext, BuildConfig.AUTHORITY, file);
                String path = FileUtils.getPath(mContext, uri);
                Logger.e("path:" + path);
            } else {
                uri = Uri.fromFile(file);
            }
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sharingIntent.setType("*/*");
            mContext.startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
        }
    }

    private void checkFileUriExposure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

}

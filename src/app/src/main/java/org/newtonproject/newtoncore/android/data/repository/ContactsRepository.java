package org.newtonproject.newtoncore.android.data.repository;

import com.moji4j.MojiConverter;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.entity.RealmContactsInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ContactsRepository implements ContactsRepositoryType {

    private String TAG = "ContactsRepository";
    private MojiConverter mojiConverter = new MojiConverter();

    private Realm getRealmInstance(Wallet wallet) {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(wallet.address + "-" + C.Key.CONTACTS + ".realm")
                .schemaVersion(C.DATABASE_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        return Realm.getInstance(config);
    }

    @Override
    public boolean addContact(Wallet wallet, ContactsInfo contactsInfo) {
        Realm realm = null;
        try {
            realm = getRealmInstance(wallet);
            RealmContactsInfo info = realm.where(RealmContactsInfo.class)
                    .equalTo("address", contactsInfo.address)
                    .findFirst();
            if(null == info) {
                realm.executeTransaction(r -> {
                    RealmContactsInfo obj = r.createObject(RealmContactsInfo.class, contactsInfo.address);
                    obj.setName(contactsInfo.name);
                });
                return true;
            }
            return false;
        } finally {
            if(null != realm) {
                realm.close();
            }
        }
    }

    @Override
    public boolean deleteContact(Wallet wallet, String address) {
        Realm realm = null;
        try {
            realm = getRealmInstance(wallet);
            RealmContactsInfo info = realm.where(RealmContactsInfo.class)
                    .equalTo("address", address)
                    .findFirst();
            if(null != info) {
                realm.executeTransaction(r -> info.deleteFromRealm());
                return true;
            }
            return false;
        } finally {
            if(null != realm) {
                realm.close();
            }
        }
    }

    @Override
    public boolean updateContact(Wallet wallet, String address, String name) {
        Realm realm = null;
        try {
            realm = getRealmInstance(wallet);
            RealmContactsInfo info = realm.where(RealmContactsInfo.class)
                    .equalTo("address", address)
                    .findFirst();
            if(null != info) {
                realm.executeTransaction(r -> info.setName(name));
                return true;
            }
            return false;
        } finally {
            if(null != realm) {
                realm.close();
            }
        }
    }

    @Override
    public ContactsInfo getContacts(Wallet wallet, String address) {
        Realm realm = null;
        try {
            realm = getRealmInstance(wallet);
            RealmContactsInfo info = realm.where(RealmContactsInfo.class)
                    .equalTo("address", address)
                    .findFirst();
            if(null != info) {
                return new ContactsInfo(info.getAddress(),info.getName());
            }
            return null;
        } finally {
            if(null != realm) {
                realm.close();
            }
        }
    }

    @Override
    public ArrayList<ContactsInfo> getAllContacts(Wallet wallet) {
        Realm realm = null;
        ArrayList<ContactsInfo> lists = new ArrayList<>();
        try {
            realm = getRealmInstance(wallet);
            RealmResults<RealmContactsInfo> infos = realm.where(RealmContactsInfo.class)
                    .findAll();
            if(null != infos && infos.size() > 0) {
                for(RealmContactsInfo info : infos) {
                    ContactsInfo contactsInfo = new ContactsInfo(info.getAddress(),info.getName());
                    if(contactsInfo.name != null && contactsInfo.name.length() > 0) {
                        char c = contactsInfo.name.charAt(0);
                        if(c <= 'z' && c >= 'a') {
                            contactsInfo.setFirstPinYin(c+"");
                        } else {
                            String[] strings = PinyinHelper.toHanyuPinyinStringArray(c);
                            if (strings != null && strings.length > 0) {
                                contactsInfo.setFirstPinYin((strings[0].charAt(0) + ""));
                            } else {
                                String s = mojiConverter.convertKanaToRomaji(c + "");
                                if (s != null && s.length() > 0) {
                                    contactsInfo.setFirstPinYin((s.charAt(0) + ""));
                                }
                            }
                        }
                    }
                    lists.add(contactsInfo);
                }
                Collections.sort(lists, new PinYinComparator());
                return lists;
            }
            return lists;
        } finally {
            if(null != realm) {
                realm.close();
            }
        }
    }

    class PinYinComparator implements Comparator<ContactsInfo>{

        @Override
        public int compare(ContactsInfo info1, ContactsInfo info2) {
            String o1 = info1.name;
            String o2 = info2.name;
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for(int i = 0; i < o1.length(); i++) {
                char c = o1.charAt(i);
                String[] strings = PinyinHelper.toHanyuPinyinStringArray(c);
                if(strings != null && strings.length > 0) {
                    sb1.append(strings[0]);
                }else{
                    String s = mojiConverter.convertKanaToRomaji(c + "");
                    if(s != null && s.length() > 0) {
                        sb1.append(s);
                    }else{
                        sb1.append(c);
                    }
                }
            }
            for(int i = 0; i < o2.length(); i++) {
                char c = o2.charAt(i);
                String[] strings = PinyinHelper.toHanyuPinyinStringArray(c);
                if(strings != null && strings.length > 0) {
                    sb2.append(strings[0]);
                }else{
                    String s = mojiConverter.convertKanaToRomaji(c + "");
                    if(s != null && s.length() > 0) {
                        sb2.append(s);
                    }else{
                        sb2.append(c);
                    }
                }
            }
            return sb1.toString().toLowerCase().compareTo(sb2.toString().toLowerCase());
        }
    }
}

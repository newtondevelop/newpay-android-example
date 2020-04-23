package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface ContactsRepositoryType {
    boolean addContact(Wallet wallet, ContactsInfo contactsInfo);
    boolean deleteContact(Wallet wallet, String address);
    boolean updateContact(Wallet wallet, String address, String name);
    ContactsInfo getContacts(Wallet wallet, String address);
    ArrayList<ContactsInfo> getAllContacts(Wallet wallet);
}

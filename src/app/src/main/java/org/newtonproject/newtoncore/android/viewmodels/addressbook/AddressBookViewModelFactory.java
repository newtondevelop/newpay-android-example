package org.newtonproject.newtoncore.android.viewmodels.addressbook;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.newtonproject.newtoncore.android.data.repository.ContactsRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-07-03--19:45
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AddressBookViewModelFactory implements ViewModelProvider.Factory {
    private ContactsRepositoryType contactsRepositoryType;
    private PreferenceRepositoryType preferenceRepositoryType;
    public AddressBookViewModelFactory(ContactsRepositoryType contactsRepositoryType,
                                       PreferenceRepositoryType preferenceRepositoryType) {
        this.contactsRepositoryType = contactsRepositoryType;
        this.preferenceRepositoryType = preferenceRepositoryType;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddressBookViewModel(contactsRepositoryType, preferenceRepositoryType);
    }
}

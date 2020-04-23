package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.repository.ContactsRepository;
import org.newtonproject.newtoncore.android.data.repository.ContactsRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.viewmodels.addressbook.AddressBookViewModelFactory;

import dagger.Module;
import dagger.Provides;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-07-03--19:48
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@Module
public class AddressBookModule {

    @Provides
    public AddressBookViewModelFactory provideAddressBookViewModelFactory(ContactsRepositoryType contactsRepositoryType,
                                                                          PreferenceRepositoryType preferenceRepositoryType) {
        return new AddressBookViewModelFactory(contactsRepositoryType, preferenceRepositoryType);
    }

    @Provides
    public ContactsRepositoryType provideContactsRepositoryType() {
        return new ContactsRepository();
    }
}

package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author weixuefeng@lubangame.com
 * @version $
 * @time: 2019-12-17--21:07
 * @description
 * @copyright (c) 2019 Newton Foundation. All rights reserved.
 */
public class NewPasswordStore implements PasswordStore {
    @Override
    public Single<String> getPassword(Wallet wallet) {
        return null;
    }

    @Override
    public Completable setPassword(Wallet wallet, String password) {
        return null;
    }

    @Override
    public Single<String> generatePassword() {
        return null;
    }
}

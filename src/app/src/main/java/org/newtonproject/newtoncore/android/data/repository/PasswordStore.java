package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface PasswordStore {
	Single<String> getPassword(Wallet wallet);
	Completable setPassword(Wallet wallet, String password);
	Single<String> generatePassword();
}

package org.newtonproject.newtoncore.android.views.splash;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.manager.AccountManager;
import org.newtonproject.newtoncore.android.viewmodels.SplashViewModel;
import org.newtonproject.newtoncore.android.viewmodels.SplashViewModelFactory;
import org.newtonproject.newtoncore.android.views.account.WelcomeActivity;
import org.newtonproject.newtoncore.android.views.home.HomeActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SplashActivity extends AppCompatActivity {
    private String TAG = "SplashActivity";
    @Inject
    SplashViewModelFactory splashViewModelFactory;
    SplashViewModel splashViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
        splashViewModel = ViewModelProviders.of(this, splashViewModelFactory)
                .get(SplashViewModel.class);
        splashViewModel.wallets().observe(this, this::onWallets);
        if (!isTaskRoot()) {
            finish();
        }
        splashViewModel.prepare();
    }

    private void onWallets(Wallet[] wallets) {
        Disposable subscribe = Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(
                        next -> {
                            Intent intent;
                            if (wallets.length == 0) {
                                intent = new Intent(this, WelcomeActivity.class);
                            } else {
                                AccountManager.getInstance().initAccounts(wallets);
                                intent = new Intent(this, HomeActivity.class);
                            }
                            Log.d(TAG, "wallet length is:" + wallets.length);
                            startActivity(intent);
                            finish();
                        },
                        error -> {
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashViewModel.onCleared();
    }
}

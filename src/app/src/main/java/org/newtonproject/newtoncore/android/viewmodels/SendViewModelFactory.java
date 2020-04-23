package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchGasSettingsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.router.GasSettingsRouter;
import org.newtonproject.newtoncore.android.router.ScanRouter;

import io.reactivex.annotations.NonNull;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SendViewModelFactory implements ViewModelProvider.Factory {

    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final FetchGasSettingsInteract fetchGasSettingsInteract;
    private final CreateTransactionInteract createTransactionInteract;
    private final PreferenceRepositoryType preferenceRepositoryType;
    private final GetDefaultWalletBalance getDefaultWalletBalance;


    private final ScanRouter scanRouter;
    private final GasSettingsRouter gasSettingsRouter;
    public SendViewModelFactory(ScanRouter scanRouter,
                                GasSettingsRouter gasSettingsRouter,
                                FindDefaultWalletInteract findDefaultWalletInteract,
                                FetchGasSettingsInteract fetchGasSettingsInteract,
                                CreateTransactionInteract createTransactionInteract,
                                PreferenceRepositoryType preferenceRepositoryType,
                                GetDefaultWalletBalance getDefaultWalletBalance) {
        this.scanRouter = scanRouter;
        this.gasSettingsRouter = gasSettingsRouter;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SendViewModel(scanRouter, gasSettingsRouter, findDefaultWalletInteract, fetchGasSettingsInteract,
                createTransactionInteract, preferenceRepositoryType, getDefaultWalletBalance);
    }
}

package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.views.account.BackupActivity;
import org.newtonproject.newtoncore.android.views.contact.AddressBookActivity;
import org.newtonproject.newtoncore.android.views.settings.AboutActivity;
import org.newtonproject.newtoncore.android.views.transaction.AddTokenActivity;
import org.newtonproject.newtoncore.android.views.transaction.ConfirmationActivity;
import org.newtonproject.newtoncore.android.views.account.CreateWalletActivity;
import org.newtonproject.newtoncore.android.views.transaction.GasSettingsActivity;
import org.newtonproject.newtoncore.android.views.home.HomeFragment;
import org.newtonproject.newtoncore.android.views.account.ImportWalletActivity;
import org.newtonproject.newtoncore.android.views.settings.LanguageSettingsActivity;
import org.newtonproject.newtoncore.android.views.home.MeFragment;
import org.newtonproject.newtoncore.android.views.transaction.SendActivity;
import org.newtonproject.newtoncore.android.views.splash.SplashActivity;
import org.newtonproject.newtoncore.android.views.settings.SwitchNetworkActivity;
import org.newtonproject.newtoncore.android.views.transaction.TokensActivity;
import org.newtonproject.newtoncore.android.views.transaction.TransactionDetailActivity;
import org.newtonproject.newtoncore.android.views.transaction.TransactionsActivity;
import org.newtonproject.newtoncore.android.views.contact.UpdateWalletPasswordActivity;
import org.newtonproject.newtoncore.android.views.account.ValidateBackupActivity;
import org.newtonproject.newtoncore.android.views.account.WalletDetailActivity;
import org.newtonproject.newtoncore.android.views.account.WalletsActivity;
import org.newtonproject.newtoncore.android.views.account.WalletsListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@Module
public abstract class BuildersModule {
	@ActivityScope
	@ContributesAndroidInjector(modules = SplashModule.class)
	abstract SplashActivity bindSplashModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = AccountsManageModule.class)
	abstract WalletsActivity bindManageWalletsModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = ImportModule.class)
	abstract ImportWalletActivity bindImportWalletModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = TransactionsModule.class)
	abstract TransactionsActivity bindTransactionsModule();

    @ActivityScope
    @ContributesAndroidInjector(modules = TransactionDetailModule.class)
    abstract TransactionDetailActivity bindTransactionDetailModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = SendModule.class)
	abstract SendActivity bindSendModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = ConfirmationModule.class)
	abstract ConfirmationActivity bindConfirmationModule();

	@ActivityScope
    @ContributesAndroidInjector(modules = TokensModule.class)
	abstract TokensActivity bindTokensModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = GasSettingsModule.class)
	abstract GasSettingsActivity bindGasSettingsModule();

	@ActivityScope
    @ContributesAndroidInjector(modules = AddTokenModule.class)
	abstract AddTokenActivity bindAddTokenActivity();

	@ActivityScope
	@ContributesAndroidInjector(modules = CreateWalletModule.class)
	abstract CreateWalletActivity bindCreateWalletActivity();

	@ActivityScope
	@ContributesAndroidInjector(modules = CreateWalletModule.class)
	abstract WalletDetailActivity bindWalletDetailActivity();

	@ActivityScope
	@ContributesAndroidInjector(modules = ValidateBackupModule.class)
	abstract ValidateBackupActivity bindValidateBackupActivity();

	@ActivityScope
	@ContributesAndroidInjector(modules = WalletsListModule.class)
	abstract WalletsListActivity bindWalletsListActivity();

	@ActivityScope
	@ContributesAndroidInjector(modules = UpdatePasswordModule.class)
	abstract UpdateWalletPasswordActivity bindUpdateWalletPasswordActivity();

	@FragmentScope
	@ContributesAndroidInjector(modules = HomeModule.class)
	abstract HomeFragment bindHomeFragment();

	@FragmentScope
	@ContributesAndroidInjector(modules = MeModule.class)
	abstract MeFragment bindMeFragment();

	@ActivityScope
	@ContributesAndroidInjector(modules = AboutModule.class)
	abstract AboutActivity bindAboutActivity();

	@ContributesAndroidInjector(modules = SwitchNetworkModule.class)
	abstract SwitchNetworkActivity bindSwitchNetworkActivity();

	@ActivityScope
	@ContributesAndroidInjector(modules = SwitchNetworkModule.class)
	abstract LanguageSettingsActivity bindLanguageSettingsActivity();

	@ActivityScope
	@ContributesAndroidInjector(modules = CreateWalletModule.class)
	abstract BackupActivity bindBackupActivity();


	@ActivityScope
	@ContributesAndroidInjector(modules = AddressBookModule.class)
	abstract AddressBookActivity bindAddressBookActivity();
}

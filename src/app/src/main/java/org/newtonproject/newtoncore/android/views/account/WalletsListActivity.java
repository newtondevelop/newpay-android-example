package org.newtonproject.newtoncore.android.views.account;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.EventMessage;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.router.ManageWalletsRouter;
import org.newtonproject.newtoncore.android.router.WalletDetailRouter;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.views.widget.adapter.WalletsListAdapter;
import org.newtonproject.newtoncore.android.viewmodels.WalletsListModel;
import org.newtonproject.newtoncore.android.viewmodels.WalletsListModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WalletsListActivity extends BaseImplActivity<WalletsListModel> {

    private static final String TAG = "WalletsListActivity";
    private RecyclerView walletsRecyclerView;

    @Inject
    WalletsListModelFactory walletsListModelFactory;

    private WalletsListAdapter walletsListAdapter;

    @Override
    protected int getActivityTitle() {
        return R.string.my_wallets;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_walletslist;
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void initViewModel() {
        mViewModel =  ViewModelProviders.of(this, walletsListModelFactory).get(WalletsListModel.class);
        mViewModel.wallets().observe(this, this::onFetchWallet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.fetchWallet();
    }

    @Override
    protected void initView() {
        walletsListAdapter = new WalletsListAdapter(this::onWalletDetail);
        walletsRecyclerView = findViewById(R.id.walletsRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        walletsRecyclerView.setLayoutManager(mLayoutManager);
        walletsRecyclerView.setHasFixedSize(true);
        walletsRecyclerView.setAdapter(walletsListAdapter);
    }

    private void onWalletDetail(Wallet wallet) {
        new WalletDetailRouter().open(this);
        EventMessage<Wallet> eventMessage = new EventMessage<>(EventMessage.WALLET_EVENT, wallet);
        EventBus.getDefault().postSticky(eventMessage);
    }

    private void onFetchWallet(Wallet[] wallets) {
        if (null == wallets || wallets.length == 0) {
            new ManageWalletsRouter().open(this, true);
            finish();
        } else {
            walletsListAdapter.setWallets(wallets);
        }
    }

}

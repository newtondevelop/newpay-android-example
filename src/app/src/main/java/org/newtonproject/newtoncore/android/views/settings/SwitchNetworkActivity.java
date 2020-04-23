package org.newtonproject.newtoncore.android.views.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.views.widget.OnHolderItemClickListener;
import org.newtonproject.newtoncore.android.views.widget.adapter.SwitchNetworkAdapter;
import org.newtonproject.newtoncore.android.viewmodels.SwitchNetworkViewModel;
import org.newtonproject.newtoncore.android.viewmodels.SwitchNetworkViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/10/20--10:35 AM
 * @description This activity can help you switch network.
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SwitchNetworkActivity extends BaseActivity implements OnHolderItemClickListener<NetworkInfo> {
    private static final String TAG = SwitchNetworkActivity.class.getName();

    @BindView(R.id.networkRecycleView)
    RecyclerView networkRecycleView;

    @Inject
    SwitchNetworkViewModelFactory factory;
    SwitchNetworkViewModel viewModel;
    private Unbinder unbinder;
    private SwitchNetworkAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_switch_network);
        toolbar();
        unbinder = ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this, factory).get(SwitchNetworkViewModel.class);
        viewModel.onNetworkLists().observe(this, this::onNetworkList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getNetworkList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void onNetworkList(NetworkInfo[] networkInfos) {
        adapter = new SwitchNetworkAdapter(networkInfos, this, viewModel.getDefaultNetwork());
        networkRecycleView.setAdapter(adapter);
        networkRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onItemClick(NetworkInfo info) {
        adapter.updateDefaultNetworkName(info.name);
        viewModel.setDefaultNetwork(info);
    }
}

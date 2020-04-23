package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.views.widget.OnHolderItemClickListener;
import org.newtonproject.newtoncore.android.views.widget.holder.BinderViewHolder;
import org.newtonproject.newtoncore.android.views.widget.holder.SwitchNetworkHolder;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/10/20--11:17 AM
 * @description SwitchNetworkAdapter.
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SwitchNetworkAdapter extends RecyclerView.Adapter<BinderViewHolder<NetworkInfo>> {

    private static final String TAG = SwitchNetworkAdapter.class.getName();
    private NetworkInfo[] networkInfos;
    private OnHolderItemClickListener<NetworkInfo> listener;
    private String defaultNetworkName;
    public SwitchNetworkAdapter(NetworkInfo[] networkInfos,
                                OnHolderItemClickListener<NetworkInfo> listener,
                                String defaultNetworkName) {
        this.networkInfos = networkInfos;
        this.listener = listener;
        this.defaultNetworkName = defaultNetworkName;
    }

    @NonNull
    @Override
    public BinderViewHolder<NetworkInfo> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SwitchNetworkHolder holder = new SwitchNetworkHolder(R.layout.item_network, parent);
        holder.setItemClickListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BinderViewHolder<NetworkInfo> holder, int position) {
        NetworkInfo info = networkInfos[position];
        Bundle bundle = new Bundle();
        bundle.putString("networkName", defaultNetworkName);
        holder.bind(info, bundle);
    }

    @Override
    public int getItemCount() {
        return networkInfos.length;
    }
    
    public void updateDefaultNetworkName(String defaultNetworkName) {
        this.defaultNetworkName = defaultNetworkName;
        notifyDataSetChanged();
    }
}

package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.ViewGroup;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.widget.holder.BinderViewHolder;
import org.newtonproject.newtoncore.android.views.widget.holder.WalletsListHolder;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WalletsListAdapter extends Adapter<BinderViewHolder> {

    private Wallet[] wallets = new Wallet[0];
    private final OnWalletItemClickListener onWalletItemClickListener;

    public WalletsListAdapter(OnWalletItemClickListener onWalletItemClickListener) {
        this.onWalletItemClickListener = onWalletItemClickListener;
    }

    @Override
    public BinderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BinderViewHolder binderViewHolder = null;
        switch (viewType) {
            case WalletsListHolder.VIEW_TPYE: {
                WalletsListHolder h = new WalletsListHolder(R.layout.item_wallet_list, parent);
                h.setOnWalletItemClickListener(this.onWalletItemClickListener);
                binderViewHolder = h;
            }
        }
        return binderViewHolder;
    }

    @Override
    public void onBindViewHolder(BinderViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case WalletsListHolder.VIEW_TPYE: {
                Wallet wallet = wallets[position];
                Bundle bundle = new Bundle();
                holder.bind(wallet, bundle);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return wallets.length;
    }

    @Override
    public int getItemViewType(int position) {
        return WalletsListHolder.VIEW_TPYE;
    }

    public interface OnWalletItemClickListener {
        void onWalletDetail(Wallet wallet);
    }

    public void setWallets(Wallet[] wallets) {
        this.wallets = wallets == null ? new Wallet[0] : wallets;
        notifyDataSetChanged();
    }
}

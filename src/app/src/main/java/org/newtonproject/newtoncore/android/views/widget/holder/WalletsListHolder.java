package org.newtonproject.newtoncore.android.views.widget.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.views.widget.adapter.WalletsListAdapter;
import org.newtonproject.newtoncore.android.utils.BalanceUtils;

import java.math.BigInteger;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WalletsListHolder extends BinderViewHolder<Wallet> implements View.OnClickListener {

    public static final int VIEW_TPYE = 1002;

    private final LinearLayout walletItem;
    private final TextView walletTextView;
    private Wallet wallet;
    private SharedPreferenceRepository sharedPreferenceRepository;
    private WalletsListAdapter.OnWalletItemClickListener onWalletItemClickListener;
    private final TextView walletBalanceTextView;

    public WalletsListHolder(int resId, ViewGroup parent) {
        super(resId, parent);
        walletItem = findViewById(R.id.walletItemLinearLayout);
        walletTextView = findViewById(R.id.walletTextView);
        walletBalanceTextView = findViewById(R.id.walletBalanceTextView);
        walletItem.setOnClickListener(this);
        sharedPreferenceRepository = new SharedPreferenceRepository(getContext());
    }


    @Override
    public void bind(@Nullable Wallet data, @NonNull Bundle addition) {
        wallet = null;
        walletTextView.setText(null);
        if (null == data) {
            return;
        }
        this.wallet = data;
        walletTextView.setText(sharedPreferenceRepository.getWalletName(wallet.address));
        String balance = sharedPreferenceRepository.getCacheBalance(wallet.address);
        walletBalanceTextView.setText(String.format("%s NEW", BalanceUtils.weiToNEW(new BigInteger(BalanceUtils.EthToWei(balance)))));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.walletItemLinearLayout:
                if(null != onWalletItemClickListener) {
                    onWalletItemClickListener.onWalletDetail(wallet);
                }
                break;
            default:
                break;
        }
    }

    public void setOnWalletItemClickListener(WalletsListAdapter.OnWalletItemClickListener onWalletItemClickListener) {
        this.onWalletItemClickListener = onWalletItemClickListener;
    }
}

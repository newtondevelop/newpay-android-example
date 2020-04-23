package org.newtonproject.newtoncore.android.views.widget.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.views.widget.OnHolderItemClickListener;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/10/20--11:19 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SwitchNetworkHolder extends BinderViewHolder<NetworkInfo> implements View.OnClickListener {


    private final TextView networkTextView;
    private final ImageView rightImageView;
    private final LinearLayout itemLayout;
    private final ImageView accessedImageView;
    private OnHolderItemClickListener<NetworkInfo> itemClickListener;
    private NetworkInfo info;

    public SwitchNetworkHolder(int resId, ViewGroup parent) {
        super(resId, parent);
        networkTextView = findViewById(R.id.networkNameTextView);
        rightImageView = findViewById(R.id.rightImageView);
        itemLayout = findViewById(R.id.itemLayout);
        accessedImageView = findViewById(R.id.accessedImageView);
        itemLayout.setOnClickListener(this);
    }

    @Override
    public void bind(@Nullable NetworkInfo info, @NonNull Bundle addition) {
        if(info == null) {
            return;
        }
        this.info = info;
        String netType = info.isMainNetwork ? "MainNet" : "TestNet";
        networkTextView.setText(String.format("%s %s", info.name, netType));
        String networkName = addition.getString("networkName");
        boolean equals = info.name.equals(networkName);
        rightImageView.setVisibility(equals ? View.VISIBLE : View.GONE);
        accessedImageView.setImageResource(info.isAccessed ? R.drawable.shape_message_dot : R.drawable.shape_message_red_dot);
    }

    public void setItemClickListener(OnHolderItemClickListener<NetworkInfo> listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itemLayout:
                if(itemClickListener != null) {
                    itemClickListener.onItemClick(info);
                }
                break;
        }
    }
}

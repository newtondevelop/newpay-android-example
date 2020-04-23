package org.newtonproject.newtoncore.android.views.widget.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.views.widget.adapter.ContactsAdapter;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ContactHolder extends BinderViewHolder<ContactsInfo> implements View.OnClickListener, View.OnLongClickListener {

    public static final int VIEW_TYPE = 1001;
    public static final int VIEW_INDICATOR_TYPE = 1002;
    private static final String TAG = "ContactHolder";
    private LinearLayout itemLinearLayout;
    private TextView nameTextView;
    private TextView addressTextView;
    private ContactsAdapter.OnFriendItemClickListener onFriendItemClickListener;
    private ContactsAdapter.OnFriendItemLongClickListener onFriendItemLongClickListener;
    private ContactsInfo info;

    public ContactHolder(int resId, ViewGroup parent) {
        super(resId, parent);
        itemLinearLayout = findViewById(R.id.itemContactsLinearLayout);
        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        itemLinearLayout.setOnClickListener(this);
        itemLinearLayout.setOnLongClickListener(this);
    }

    @Override
    public void bind(@Nullable ContactsInfo data, @NonNull Bundle addition) {
        info = null;
        nameTextView.setText(null);
        addressTextView.setText(null);
        if(null == data) {
            return;
        }
        this.info = data;
        addressTextView.setText(info.address);
        nameTextView.setText(info.name);
    }


    public void setOnFriendItemClickListener(ContactsAdapter.OnFriendItemClickListener onFriendItemClickListener) {
        this.onFriendItemClickListener = onFriendItemClickListener;
    }

    public void setOnFriendItemLongClickListener(ContactsAdapter.OnFriendItemLongClickListener onFriendItemLongClickListener) {
        this.onFriendItemLongClickListener = onFriendItemLongClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itemContactsLinearLayout:
                if(null != onFriendItemClickListener) {
                    onFriendItemClickListener.onResult(addressTextView.getText().toString().trim());
                }
                break;
                default:
                    break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.itemContactsLinearLayout:
                if(null != onFriendItemLongClickListener) {
                    onFriendItemLongClickListener.onLongClickResult(addressTextView.getText().toString().trim(), itemLinearLayout);
                }
                break;
            default:
                break;
        }
        return false;
    }
}

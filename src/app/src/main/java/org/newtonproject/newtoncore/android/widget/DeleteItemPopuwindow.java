package org.newtonproject.newtoncore.android.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.widget.OnDeleteFriendListener;
import org.newtonproject.newtoncore.android.views.widget.OnUpdateFriendListener;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class DeleteItemPopuwindow extends PopupWindow implements View.OnClickListener {
    private ViewGroup view;
    private OnDeleteFriendListener onDeleteFriendListener;
    private OnUpdateFriendListener onUpdateFriendListener;

    public DeleteItemPopuwindow(Context context) {
        LayoutInflater inflater = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (ViewGroup) inflater.inflate(R.layout.popuwindow_delete_item, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        initViews();
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
    }

    private void initViews() {
        view.findViewById(R.id.deleteTextView).setOnClickListener(this);
        view.findViewById(R.id.updateNameTextView).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteTextView:
                if(null != onDeleteFriendListener) {
                    onDeleteFriendListener.deleteFriend();
                }
                break;
            case R.id.updateNameTextView:
                if(null != onUpdateFriendListener) {
                    onUpdateFriendListener.updateFriend();
                }
                break;
            default:
                break;
        }
    }

    public void setOnDeleteFriendListener(OnDeleteFriendListener deleteFriendListener) {
        this.onDeleteFriendListener = deleteFriendListener;
    }

    public void setOnUpdateFriendListener(OnUpdateFriendListener updateFriendListener) {
        this.onUpdateFriendListener = updateFriendListener;
    }
}

package org.newtonproject.newtoncore.android.widget;

import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.base.BaseDialog;
import org.newtonproject.newtoncore.android.views.base.ViewHolder;
import org.newtonproject.newtoncore.android.views.widget.adapter.CheckItemAdapter;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/21--2:40 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class CheckDialog extends BaseDialog {

    RecyclerView recyclerView;

    @StringRes int title = -1;

    Button continueButton;
    private CheckItemAdapter adapter = new CheckItemAdapter();
    private int mContinueBgRes = -1;
    private boolean mNeedChecked = true;
    private int continueText = -1;
    private OnCancelListener onCancelListener;

    @Override
    public int setUpLayoutId() {
        return R.layout.dialog_security_information;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
        recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.setOnCheckBoxChangedListener(isAllChecked -> {
            if(isAllChecked) {
                continueButton.setEnabled(true);
            }else{
                continueButton.setEnabled(false);
            }
        });
        recyclerView.setAdapter(adapter);
        continueButton = holder.getView(R.id.continueButton);
        if(!mNeedChecked) {
            continueButton.setEnabled(true);
        }
        TextView titleTextView = holder.getView(R.id.titleTextView);
        if(title != -1) {
            titleTextView.setText(title);
        }
        if(mContinueBgRes != -1) {
            continueButton.setBackgroundResource(mContinueBgRes);
            continueButton.setTextColor(Color.WHITE);
            continueButton.setEnabled(true);
        }
        if(continueText != -1) {
            continueButton.setText(continueText);
        }
        holder.setOnClickListener(R.id.continueButton, v -> {
            if(listener != null) {
                listener.onContinue();
            }
            dismiss();
        });
        holder.setOnClickListener(R.id.cancelButton, v -> {
            if(onCancelListener != null) {
                onCancelListener.onCancel();
            }
            dismiss();
        });
    }

    public void setTitle(@StringRes int titleId) {
        title = titleId;
    }

    public void setContinueText(@StringRes int text) {
        continueText = text;
    }

    public void setData(ArrayList<String> datas) {
        adapter.setDatas(datas);
    }

    public void setNeedChecked(boolean needChecked) {
        mNeedChecked = needChecked;
        adapter.setNeedChecked(needChecked);
    }

    public void setContinueBg(@DrawableRes int backgroundRes) {
        mContinueBgRes = backgroundRes;
    }

    private OnContinueListener listener;

    public void setOnContinueListener(OnContinueListener continueListener) {
        this.listener = continueListener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public interface OnContinueListener {
       void onContinue();
    }

    public interface OnCancelListener {
        void onCancel();
    }
}

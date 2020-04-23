package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.response.StakingHistory;
import org.newtonproject.newtoncore.android.views.widget.holder.BinderViewHolder;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class CheckItemAdapter extends RecyclerView.Adapter<BinderViewHolder>{

    private ArrayList<String> datas = new ArrayList<>();
    private boolean mNeedChecked = true;

    @NonNull
    @Override
    public BinderViewHolder<StakingHistory.StakingInfo> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BinderViewHolder holder = new ViewHolder(R.layout.item_check_dialog, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BinderViewHolder holder, int position) {
        holder.bind(datas.get(position));
    }

    public void setDatas(ArrayList<String> list) {
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        datas.clear();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setNeedChecked(boolean needChecked) {
        mNeedChecked = needChecked;
    }

    public class ViewHolder extends BinderViewHolder<String>{
        private TextView textView;
        private CheckBox checkBox;
        private LinearLayout itemLayout;

        public ViewHolder(int resId, ViewGroup parent) {
            super(resId, parent);
            textView = findViewById(R.id.textView);
            checkBox = findViewById(R.id.checkbox);
            itemLayout = findViewById(R.id.itemLayout);
            itemLayout.setOnClickListener(listener -> checkBox.setChecked(!checkBox.isChecked()));
        }

        @Override
        public void bind(@Nullable String data, @NonNull Bundle addition) {
            if(data == null) return;
            textView.setText(data);
            checkBox.setVisibility(mNeedChecked ? View.VISIBLE : View.GONE);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    totalCheckedIndex = totalCheckedIndex + 1;
                }else{
                    totalCheckedIndex = totalCheckedIndex - 1;
                }
                if(listener != null) {
                    listener.onChanged(totalCheckedIndex == datas.size());
                }
            });
        }
    }

    public interface OnCheckboxChangedListener {
        void onChanged(boolean isAllChecked);
    }

    private OnCheckboxChangedListener listener;

    private int totalCheckedIndex = 0;

    public void setOnCheckBoxChangedListener(OnCheckboxChangedListener checkBoxChangedListener) {
        this.listener = checkBoxChangedListener;
    }
}

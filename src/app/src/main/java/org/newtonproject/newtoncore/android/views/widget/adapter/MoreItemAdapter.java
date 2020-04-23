package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.widget.holder.BinderViewHolder;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-07-04--14:57
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class MoreItemAdapter extends RecyclerView.Adapter<BinderViewHolder<String>> {

    private ArrayList<String> items;

    public MoreItemAdapter(ArrayList<String> list) {
        if(list == null || list.size() == 0) {
            items = new ArrayList<>();
        } else {
            items = list;
        }
    }

    @NonNull
    @Override
    public BinderViewHolder<String> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(R.layout.item_more, parent);
        if(listener != null) {
            viewHolder.setOnItemClickListener(listener);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BinderViewHolder<String> holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends BinderViewHolder<String>{
        private TextView itemsTextView;
        public ViewHolder(int resId, ViewGroup parent) {
            super(resId, parent);
            itemsTextView = findViewById(R.id.itemTextView);
        }

        @Override
        public void bind(@Nullable String data, @NonNull Bundle addition) {
            itemsTextView.setText(data);
            if(listener != null) {
                itemsTextView.setOnClickListener(click -> listener.onItemClick(data));
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
        private OnItemClickListener listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String data);
    }

}

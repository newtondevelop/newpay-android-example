package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.widget.OnTransactionClickListener;
import org.newtonproject.newtoncore.android.views.widget.entity.DateSortedItem;
import org.newtonproject.newtoncore.android.views.widget.entity.SortedItem;
import org.newtonproject.newtoncore.android.views.widget.entity.TimestampSortedItem;
import org.newtonproject.newtoncore.android.views.widget.entity.TransactionSortedItem;
import org.newtonproject.newtoncore.android.views.widget.holder.BinderViewHolder;
import org.newtonproject.newtoncore.android.views.widget.holder.FooterViewHolder;
import org.newtonproject.newtoncore.android.views.widget.holder.TransactionDateHolder;
import org.newtonproject.newtoncore.android.views.widget.holder.TransactionHolder;
import org.newtonproject.newtoncore.android.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionsAdapter extends RecyclerView.Adapter<BinderViewHolder> {

    private String TAG = "TransactionsAdapter";
    private ArrayList<String> dateList = new ArrayList<>();
    private HashMap<String, Integer> datePositionMap = new HashMap<>();
    private Wallet mWallet;

    private final SortedList<SortedItem> items = new SortedList<>(SortedItem.class, new SortedList.Callback<SortedItem>() {
        @Override
        public int compare(SortedItem left, SortedItem right) {
            return left.compare(right);
        }

        @Override
        public boolean areContentsTheSame(SortedItem oldItem, SortedItem newItem) {
            return oldItem.areContentsTheSame(newItem);
        }

        @Override
        public boolean areItemsTheSame(SortedItem left, SortedItem right) {
            return left.areItemsTheSame(right);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    private final OnTransactionClickListener onTransactionClickListener;
    private FooterViewHolder.OnLoadMoreClickListener onLoadMoreClickListener;
    private FooterViewHolder footerViewHolder;

    public TransactionsAdapter(OnTransactionClickListener onTransactionClickListener, Wallet wallet) {
        this.onTransactionClickListener = onTransactionClickListener;
        mWallet = wallet;
    }

    @Override
    public BinderViewHolder<?> onCreateViewHolder(ViewGroup parent, int viewType) {
        BinderViewHolder holder = null;
        switch (viewType) {
            case TransactionHolder.VIEW_TYPE: {
                TransactionHolder transactionHolder
                        = new TransactionHolder(R.layout.item_transaction, parent);
                transactionHolder.setOnTransactionClickListener(onTransactionClickListener);
                holder = transactionHolder;
            } break;
            case TransactionDateHolder.VIEW_TYPE: {
                holder = new TransactionDateHolder(R.layout.item_transactions_date_head, parent);
            } break;
            case FooterViewHolder.VIEW_TPYE: {
                footerViewHolder = new FooterViewHolder(R.layout.item_footer, parent);
                if(onLoadMoreClickListener != null) {
                    footerViewHolder.setOnLoadMoreClickListener(onLoadMoreClickListener);
                }
                holder = footerViewHolder;
            } break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BinderViewHolder holder, int position) {
        if (position != items.size() && items.size() != 0) {
            Bundle addition = new Bundle();
            addition.putString(TransactionHolder.DEFAULT_ADDRESS_ADDITIONAL, mWallet == null ? null : mWallet.address);
            addition.putString(TransactionHolder.DEFAULT_SYMBOL_ADDITIONAL, C.NEW_SYMBOL);
            holder.bind(items.get(position).value, addition);
        } else {
            holder.bind(null);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == items.size() || items.size() == 0) {
            return FooterViewHolder.VIEW_TPYE;
        } else {
            return items.get(position).viewType;
        }
    }

    public HashMap<String, Integer> addTransactions(Transaction[] transactions) {
        items.beginBatchedUpdates();
        for (Transaction transaction : transactions) {
            TransactionSortedItem sortedItem = new TransactionSortedItem(
                    TransactionHolder.VIEW_TYPE, transaction, TimestampSortedItem.DESC);
            items.add(sortedItem);
            DateSortedItem round = DateSortedItem.round(transaction.timeStamp);
            int datePositionIndex = items.add(round);
            String value = DateUtils.getDateShortTime(transaction.timeStamp);
            datePositionMap.put(value, datePositionIndex);
        }
        items.endBatchedUpdates();
        return datePositionMap;
    }

    public void setWallet(Wallet wallet) {
        mWallet = wallet;
    }

    public void setOnLoadMoreClickListener(FooterViewHolder.OnLoadMoreClickListener moreClickListener) {
        onLoadMoreClickListener = moreClickListener;
    }

    public void clear() {
        dateList.clear();
        datePositionMap.clear();
        items.clear();
    }

    public void showLoadingMore() {
        if(footerViewHolder != null) {
            footerViewHolder.showLoadingView();
        }
    }

    public void showEndView() {
        if(footerViewHolder != null) {
            footerViewHolder.showEndView();
        }
    }

}

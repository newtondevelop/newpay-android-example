package org.newtonproject.newtoncore.android.views.widget.entity;

import android.text.format.DateUtils;

import org.newtonproject.newtoncore.android.data.entity.common.Transaction;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionSortedItem extends TimestampSortedItem<Transaction> {

    public TransactionSortedItem(int viewType, Transaction value, int order) {
        super(viewType, value, 0, order);
    }

    @Override
    public int compare(SortedItem other) {
        int res = super.compare(other);
        if(res == 0) {
            res = -1;
        }
        return res;
//        return other.viewType == TransactionHolder.VIEW_TYPE ||
//                ? super.compare(other)
//                : weight - other.weight;
    }

    @Override
    public boolean areContentsTheSame(SortedItem newItem) {
        if (viewType == newItem.viewType) {
            Transaction transaction = (Transaction)newItem.value;
            return value.hash.equals(transaction.hash) && value.timeStamp == transaction.timeStamp;
        }
        return false;
    }

    @Override
    public boolean areItemsTheSame(SortedItem other) {
        return viewType == other.viewType;
    }

    @Override
    public Date getTimestamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(value.timeStamp * DateUtils.SECOND_IN_MILLIS);
        return calendar.getTime();
    }
}

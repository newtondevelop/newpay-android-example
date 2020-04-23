package org.newtonproject.newtoncore.android.views.widget.entity;

import android.text.format.DateUtils;

import org.newtonproject.newtoncore.android.views.widget.holder.TransactionDateHolder;

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
public class DateSortedItem extends TimestampSortedItem<Date> {
    public DateSortedItem(Date value) {
        super(TransactionDateHolder.VIEW_TYPE, value, 0, DESC);
    }

    @Override
    public Date getTimestamp() {
        return value;
    }

    @Override
    public boolean areContentsTheSame(SortedItem newItem) {
        return viewType == newItem.viewType && value.equals(((TimestampSortedItem) newItem).value);
    }

    @Override
    public boolean areItemsTheSame(SortedItem other) {
        return viewType == other.viewType;
    }

    public static DateSortedItem round(long timeStampInSec) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(timeStampInSec * DateUtils.SECOND_IN_MILLIS);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return new DateSortedItem(calendar.getTime());
    }
}

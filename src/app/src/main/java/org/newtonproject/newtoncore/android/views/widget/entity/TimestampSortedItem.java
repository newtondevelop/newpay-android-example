package org.newtonproject.newtoncore.android.views.widget.entity;

import java.util.Date;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public abstract class TimestampSortedItem<T> extends SortedItem<T> {

    public static final int ASC = 1;
    public static final int DESC = -1;

    private static final int IS_TIMESTAMP_TAG = 1;

    private final int order;


    public TimestampSortedItem(int viewType, T value, int weight, int order) {
        super(viewType, value, weight);
        tags.add(IS_TIMESTAMP_TAG);
        this.order = order;
    }

    public abstract Date getTimestamp();

    @Override
    public int compare(SortedItem other) {
        if (other.tags.contains(IS_TIMESTAMP_TAG)) {
            TimestampSortedItem otherTimestamp = (TimestampSortedItem) other;
            return order * (getTimestamp().compareTo(otherTimestamp.getTimestamp()));/*
                    ? 1 : getTimestamp() == otherTimestamp.getTimestamp() ? 0 : -1);*/
        }
        return Integer.MIN_VALUE;
    }
}

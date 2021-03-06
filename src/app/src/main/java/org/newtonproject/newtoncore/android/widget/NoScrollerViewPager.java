package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/29--PM 9:28
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NoScrollerViewPager extends ViewPager {

    public NoScrollerViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}

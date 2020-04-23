package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/11/15--10:18 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NoDragSwitchCompat extends SwitchCompat {

    public NoDragSwitchCompat(Context context) {
        super(context);
    }

    public NoDragSwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoDragSwitchCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}

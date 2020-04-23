package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.newtonproject.newtoncore.android.R;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/14--4:17 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class IconTextView extends FrameLayout {

    public IconTextView(@NonNull Context context) {
        super(context);
    }

    public IconTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IconTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_icon_textview, this, false);
        addView(view);
    }
}

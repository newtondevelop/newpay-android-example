package org.newtonproject.newtoncore.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.utils.StringUtil;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/30--7:14 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@SuppressLint("AppCompatCustomView")
public class EasyCopyTextView extends TextView {

    private TextView linkedTextView;
    private int textViewId;

    public EasyCopyTextView(Context context) {
        this(context, null);
    }

    public EasyCopyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyCopyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EasyCopyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.easyCopyTextView);
        if(null != attrArray) {
            textViewId = attrArray.getResourceId(R.styleable.easyCopyTextView_link_text, Integer.MAX_VALUE);
            attrArray.recycle();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(v -> {
            if(textViewId != Integer.MAX_VALUE) {
                linkedTextView = getRootView().findViewById(textViewId);
            }
            String content;
            if(linkedTextView != null) {
                content = linkedTextView.getText().toString().trim();
            } else {
                content = getText().toString().trim();
            }
            StringUtil.copyContent(getContext(), content);
        });
    }
}

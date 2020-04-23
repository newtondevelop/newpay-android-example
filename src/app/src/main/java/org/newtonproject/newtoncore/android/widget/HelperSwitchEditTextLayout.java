package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;

/**
 * @author zhaominmin@diynova.com
 * @version $
 * @time: 2019/12/5.15:08
 * @description
 * @copyright (c) 2019 Newton Foundation. All rights reserved.
 */
public class HelperSwitchEditTextLayout extends HelperEditTextLayout {

    public ConstraintLayout exchangeLayout;
    public ImageView exchangeImageView;
    public TextView useCoinTypeTextView;
    public TextView symbolTextView;

    public HelperSwitchEditTextLayout(Context context) {
        super(context);
    }

    public HelperSwitchEditTextLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HelperSwitchEditTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        exchangeLayout = view.findViewById(R.id.exchangeLayout);
        exchangeImageView = view.findViewById(R.id.exchangeImageView);
        useCoinTypeTextView = view.findViewById(R.id.useCoinTypeTextView);
        symbolTextView = view.findViewById(R.id.symbolTextView);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_helper_switch_edittext;
    }
}

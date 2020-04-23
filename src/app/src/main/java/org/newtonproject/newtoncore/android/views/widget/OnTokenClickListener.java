package org.newtonproject.newtoncore.android.views.widget;

import android.view.View;

import org.newtonproject.newtoncore.android.data.entity.common.Token;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface OnTokenClickListener {
    void onTokenClick(View view, Token token);
}

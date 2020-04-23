package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.intro.WebViewActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UserTermsRouter {
    public void open(Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(C.EXTRA_TITLE, context.getString(R.string.user_of_terms));
        intent.putExtra(C.EXTRA_URL, C.USER_OF_TERMS_URL);
        context.startActivity(intent);
    }
}

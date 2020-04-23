package org.newtonproject.newtoncore.android.views.widget;

import android.app.Activity;
import android.text.style.ClickableSpan;
import android.view.View;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.router.UserTermsRouter;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UserTermsSpanListener extends ClickableSpan {
    private int type;
    private Activity context;

    public UserTermsSpanListener(int type, Activity context) {
        super();
        this.type = type;
        this.context = context;
    }

    @Override
    public void onClick(View widget) {
        switch (type) {
            case C.TERMS_TYPE:
                new UserTermsRouter().open(context);
                break;
            case C.POLICY_TYPE:
                //new PrivacyPolicyRouter().open(context);
                break;
            default:
                break;
        }
    }
}

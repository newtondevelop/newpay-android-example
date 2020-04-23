package org.newtonproject.newtoncore.android.router;

import android.app.Activity;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.views.transaction.SetAmountActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SetAmountRouter {

    public void openForResult(Activity context) {
        Intent intent = new Intent(context, SetAmountActivity.class);
        context.startActivityForResult(intent, C.REQUEST_CODE_SET_AMOUNT);
    }
}

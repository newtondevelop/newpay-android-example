package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.views.transaction.TransactionsActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionsRouter {
    public void open(Context context, boolean isClearStack) {
        Intent intent = new Intent(context, TransactionsActivity.class);
        if (isClearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }
}

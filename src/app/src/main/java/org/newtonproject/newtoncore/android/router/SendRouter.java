package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.views.transaction.SendActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SendRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, SendActivity.class);
        context.startActivity(intent);
    }

    public void openWithAddress(Context context, String address, String amount) {
        Intent intent = new Intent(context, SendActivity.class);
        if (null != address) {
            intent.putExtra(C.EXTRA_ADDRESS, address);
        }
        if (null != amount) {
            intent.putExtra(C.EXTRA_AMOUNT, amount);
        }
        context.startActivity(intent);
    }
}

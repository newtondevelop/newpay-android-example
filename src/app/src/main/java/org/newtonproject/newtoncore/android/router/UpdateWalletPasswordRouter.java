package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.views.contact.UpdateWalletPasswordActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UpdateWalletPasswordRouter {

    public void open(Context context, String address) {
        Intent intent = new Intent(context, UpdateWalletPasswordActivity.class);
        intent.putExtra(C.EXTRA_ADDRESS, address);
        context.startActivity(intent);
    }
}

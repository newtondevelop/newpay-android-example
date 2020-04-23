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
public class SendTokenRouter {
    public void open(Context context, String address, String symbol, int decimals) {
        Intent intent = new Intent(context, SendActivity.class);
        intent.putExtra(C.EXTRA_SENDING_TOKENS, true);
        intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, address);
        intent.putExtra(C.EXTRA_SYMBOL, symbol);
        intent.putExtra(C.EXTRA_DECIMALS, decimals);
        context.startActivity(intent);
    }
}

package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.transaction.TokensActivity;

import static org.newtonproject.newtoncore.android.C.Key.WALLET;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class MyTokensRouter {

    public void open(Context context, Wallet wallet) {
        Intent intent = new Intent(context, TokensActivity.class);
        intent.putExtra(WALLET, wallet);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}

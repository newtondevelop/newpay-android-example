package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.views.transaction.TransactionDetailActivity;

import static org.newtonproject.newtoncore.android.C.Key.TRANSACTION;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionDetailRouter {

    public void open(Context context, Transaction transaction) {
        Intent intent = new Intent(context, TransactionDetailActivity.class);
        intent.putExtra(TRANSACTION, transaction);
        context.startActivity(intent);
    }

    public void openByTxid(Context context, String txid) {
        Intent intent = new Intent(context, TransactionDetailActivity.class);
        intent.putExtra(C.EXTRA_TXID, txid);
        Log.e("HomeModel"," START ACTIVITYS");
        context.startActivity(intent);
    }
}

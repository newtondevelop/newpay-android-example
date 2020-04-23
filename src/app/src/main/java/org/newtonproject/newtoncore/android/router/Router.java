package org.newtonproject.newtoncore.android.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author weixuefeng@lubangame.com
 * @version 0.3.1
 * @time: 2018/9/27--PM 4:15
 * @description router
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class Router {

    public void open(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public void openWithData(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("data", bundle);
        context.startActivity(intent);
    }

    public void openForResult(Activity context, Class<?> cls, int requestCode) {
        Intent intent = new Intent(context, cls);
        context.startActivityForResult(intent, requestCode);
    }

    public void openAndClear(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}

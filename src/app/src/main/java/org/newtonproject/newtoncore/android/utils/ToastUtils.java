package org.newtonproject.newtoncore.android.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ToastUtils make toast fast.
 * @version 0.3
 * @author weixuefeng@lubangame.com
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ToastUtils {

    private static Toast toast;

    public static void showToastShort(Context context, String msg) {

        final Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 800);
    }

    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToastCenterShort(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0, DPUtils.dp2Px(context,150));
        }
        toast.setText(msg);
        toast.show();
    }

    public static void showNormalToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showNormalToast(Context context, @StringRes int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToastCenterWithBackground(Context context,String msg){
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            LinearLayout linearLayout = (LinearLayout) toast.getView();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPUtils.getScreenWidth(context)/2, DPUtils.getScreenWidth(context)/2);
            linearLayout.setLayoutParams(params);
            linearLayout.setBackgroundColor(Color.parseColor("#ECEDEB"));
            TextView tv = (TextView) linearLayout.getChildAt(0);
            tv.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setGravity(Gravity.CENTER);
            tv.setLines(3);
            tv.setTextSize(DPUtils.dp2Px(context,5));
        }
        toast.setText(msg);
        toast.show();
    }
}


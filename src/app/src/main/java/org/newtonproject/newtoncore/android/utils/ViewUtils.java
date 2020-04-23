package org.newtonproject.newtoncore.android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;

import java.util.HashMap;

import static org.newtonproject.newtoncore.android.utils.StringUtil.getString;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ViewUtils {

    public static void buttonToggle(TextView button, boolean status, String text) {
        if(button != null) {
            if(text != null) {
                button.setText(text);
            }
            button.setEnabled(status);
            button.setClickable(status);
        }
    }

    public static void buttonToggle(TextView button, boolean status,@StringRes int id) {
        if(button != null) {
            button.setText(id);
            button.setEnabled(status);
            button.setClickable(status);
        }
    }

    public static Bitmap createQRImage(Activity context, String qrContent, float scale) {
        Point size = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(size);
        int imageSize = (int) (size.x * scale);
        try {
            HashMap<EncodeHintType, Integer> map = new HashMap<>();
            map.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    qrContent,
                    BarcodeFormat.QR_CODE,
                    imageSize,
                    imageSize,
                    map);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.error_fail_generate_qr), Toast.LENGTH_SHORT)
                    .show();
        }
        return null;
    }

    public static Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache(true);
    }

    public static boolean checkTextViewNull(View view, Context context, @StringRes int errorMsg) {
        return checkTextViewNull(view, context, context.getResources().getString(errorMsg));
    }

    public static boolean checkTextViewNull(View view, Context context, String errorMsg) {
        if(null == view) {
            throw new IllegalArgumentException("view is null");
        }
        String text = null;
        if(view instanceof TextView) {
            text = ((TextView) view).getText().toString().trim();
        }else {
            throw new IllegalArgumentException("view has not string attribute.");
        }
        if(TextUtils.isEmpty(text)) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static int getTransactionStatusIcon(int status) {
        int resId = 0;
        switch (status) {
            case C.TRANSACTION_STATUS.MOVED:
                resId = R.mipmap.icon_trans_exch;
                break;
            case C.TRANSACTION_STATUS.RECEIVE:
                resId = R.mipmap.icon_trans_rcvd;
                break;
            case C.TRANSACTION_STATUS.SEND:
                resId =R.mipmap.icon_trans_sent;
                break;
            default:
                resId = 0;
                break;
        }
        return resId;
    }

    public static TranslateAnimation enterAnimation() {
        TranslateAnimation enterAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        enterAnimation.setDuration(500);
        return enterAnimation;
    }

    public static TranslateAnimation leaveAnimation() {
        TranslateAnimation leaveAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        leaveAnimation.setDuration(500);
        return leaveAnimation;
    }

    public static BitmapDrawable getBlurBackground() {

        Bitmap bitmap = Bitmap.createBitmap(10, 10,
                Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor("#FFFFFF"));
        BitmapDrawable bitmapDrawable = new BitmapDrawable(null, fastblur(bitmap, 20));
        bitmapDrawable.setAlpha(245);
        return bitmapDrawable;
    }

    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    public static void addFlag(String flagName) {
        Answers.getInstance().logCustom(new CustomEvent(flagName));
    }

}

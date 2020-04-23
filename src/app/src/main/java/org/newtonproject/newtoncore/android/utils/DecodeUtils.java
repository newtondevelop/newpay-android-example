package org.newtonproject.newtoncore.android.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.device.ScanActivity;
import org.newtonproject.newtoncore.android.views.intro.WebViewActivity;

import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static org.newtonproject.newtoncore.android.utils.StringUtil.getString;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class DecodeUtils {
    private static final String TAG = "DecodeUtils";

    public static class DecodeAsyncTask extends AsyncTask<Bitmap, Integer, Result> {

        private WeakReference<Context> mContext;
        private Result result;

        public DecodeAsyncTask(Context mContext) {
            this.mContext = new WeakReference<Context>(mContext);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Result doInBackground(Bitmap... bitmaps) {
            result = decodeFromPicture(bitmaps[0]);
            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (result != null) {
                String text = result.getText();
                if (!TextUtils.isEmpty(text)) {
                    Intent intent = new Intent(Intents.Scan.ACTION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.putExtra(Intents.Scan.RESULT, text);
                    if (mContext.get() instanceof ScanActivity){
                        if(text.startsWith("http://") || text.startsWith("https://")) {
                            Intent intent1 = new Intent(mContext.get(), WebViewActivity.class);
                            intent1.putExtra(C.EXTRA_URL, text);
                            mContext.get().startActivity(intent1);
                            ((ScanActivity) mContext.get()).finish();
                        } else {
                            ((ScanActivity) mContext.get()).setResult(RESULT_OK, intent);
                            ((ScanActivity) mContext.get()).finish();
                        }
                    }
                }
            } else {
                Toast.makeText(mContext.get(), getString(R.string.decode_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


    private static Result decodeFromPicture(Bitmap bitmap) {
        if (bitmap == null) return null;
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        int[] pix = new int[picWidth * picHeight];
        bitmap.getPixels(pix, 0, picWidth, 0, 0, picWidth, picHeight);
        //构造LuminanceSource对象
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(picWidth
                , picHeight, pix);
        BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //因为解析的条码类型是二维码，所以这边用QRCodeReader最合适。
        QRCodeReader qrCodeReader = new QRCodeReader();
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, true);
        Result result = null;
        try {
            result = qrCodeReader.decode(bb, hints);
            return result;
        } catch (NotFoundException | ChecksumException | FormatException e) {
            e.printStackTrace();
            return null;
        }
    }














}

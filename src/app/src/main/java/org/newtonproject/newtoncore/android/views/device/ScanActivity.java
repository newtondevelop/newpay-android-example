package org.newtonproject.newtoncore.android.views.device;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.utils.DecodeUtils;
import org.newtonproject.newtoncore.android.utils.FileUtils;
import org.newtonproject.newtoncore.android.utils.UriUtils;

import java.lang.ref.WeakReference;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ScanActivity extends CaptureActivity implements View.OnClickListener {
    private static final int MESSAGE_DECODE_FROM_BITMAP = 1;
    private static final String TAG = ScanActivity.class.getSimpleName();
    TextView moreImgeView;
    private View statusBarView;

    private DecodeHandler mHandler;
    private DecoratedBarcodeView decoratedBarcodeView;

    private static class DecodeHandler extends Handler {
        private WeakReference<ScanActivity> activity;

        DecodeHandler(ScanActivity mainActivityWeakReference) {
            activity = new WeakReference<ScanActivity>(mainActivityWeakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ScanActivity activity = this.activity.get();
            if (activity != null) {
                if (msg.what == activity.MESSAGE_DECODE_FROM_BITMAP) {
                    Bitmap bm = (Bitmap) msg.obj;
                    DecodeUtils.DecodeAsyncTask decodeAsyncTask = new DecodeUtils.DecodeAsyncTask(activity);
                    decodeAsyncTask.execute(bm);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                if (isStatusBar()) {
                    initStatusBar();
                    getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            initStatusBar();
                        }
                    });
                }
                return false;
            }
        });
        initView();
        mHandler = new DecodeHandler(this);
    }

    private void initView() {
        ImageView backImageView = findViewById(R.id.backImageView);
        moreImgeView = findViewById(R.id.moreImgeView);
        moreImgeView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
    }

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_scan);
        decoratedBarcodeView = findViewById(R.id.zxing_barcode_scanner);
        decoratedBarcodeView.getStatusView().setPadding(0, 0 , 0, 200);
        decoratedBarcodeView.setStatusText(getString(R.string.qr_tip));
        return decoratedBarcodeView;
    }

    private void initStatusBar() {
        if (statusBarView == null) {
            int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
            statusBarView = getWindow().findViewById(identifier);
        }
        if (statusBarView != null) {
            statusBarView.setBackgroundResource(R.color.bgColor);
        }
    }

    protected boolean isStatusBar() {
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.moreImgeView:
                clickOpenPicture();
            default:
                break;
        }
    }

    public void clickOpenPicture() {
        int checked = ContextCompat.checkSelfPermission(this
                , Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checked == PackageManager.PERMISSION_GRANTED) {
            openPicture();
        } else {
            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, C.PERMISSION_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void openPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, C.REQUEST_CODE_GET_PIC_URI);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == C.PERMISSION_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openPicture();
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case C.REQUEST_CODE_GET_PIC_URI:
                    Uri uri = data.getData();
                    String imagePath = UriUtils.getPicturePathFromUri(this, uri);
                    Bitmap bitmap = FileUtils.compressPicture(imagePath);
                    Message message = mHandler.obtainMessage(MESSAGE_DECODE_FROM_BITMAP, bitmap);
                    mHandler.sendMessage(message);
                    break;
            }
        }
    }
}

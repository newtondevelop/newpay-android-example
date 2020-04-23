package org.newtonproject.newtoncore.android.router;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;

import org.newtonproject.newtoncore.android.views.device.ScanActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ScanRouter {

    public void open(Context context) {
        IntentIntegrator integrator = new IntentIntegrator((Activity) context);
        openScan(integrator);
    }

    public void openFromFragment(Fragment fragment) {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(fragment);
        openScan(integrator);
    }

    private void openScan(IntentIntegrator integrator) {
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }
}

package org.newtonproject.newtoncore.android.router;


import android.app.Activity;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.GasSettings;
import org.newtonproject.newtoncore.android.views.transaction.GasSettingsActivity;
import org.newtonproject.newtoncore.android.viewmodels.GasSettingsViewModel;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class GasSettingsRouter {
    public void open(Activity context, GasSettings gasSettings) {
        Intent intent = new Intent(context, GasSettingsActivity.class);
        intent.putExtra(C.EXTRA_GAS_PRICE, gasSettings.gasPrice.toString());
        intent.putExtra(C.EXTRA_GAS_LIMIT, gasSettings.gasLimit.toString());
        context.startActivityForResult(intent, GasSettingsViewModel.SET_GAS_SETTINGS);
    }
}

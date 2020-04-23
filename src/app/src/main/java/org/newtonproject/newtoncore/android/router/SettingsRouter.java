package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.views.settings.NewPaySettingsActivity;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SettingsRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, NewPaySettingsActivity.class);
        context.startActivity(intent);
    }
}

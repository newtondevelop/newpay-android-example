package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ExternalBrowserRouter {

    public void open(Context context, Uri uri) {
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(launchBrowser);
    }
}

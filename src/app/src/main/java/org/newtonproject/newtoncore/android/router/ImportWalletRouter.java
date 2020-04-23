package org.newtonproject.newtoncore.android.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.views.account.ImportWalletActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ImportWalletRouter {

	public void open(Context context) {
		Intent intent = new Intent(context, ImportWalletActivity.class);
		context.startActivity(intent);
	}

	public void openForResult(Activity activity, int requestCode) {
		Intent intent = new Intent(activity, ImportWalletActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}
}

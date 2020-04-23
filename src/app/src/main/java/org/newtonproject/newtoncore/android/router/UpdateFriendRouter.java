package org.newtonproject.newtoncore.android.router;

import android.app.Activity;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.user.UpdateFriendActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UpdateFriendRouter {

    public void openForResult(Activity context, int requestCode, Wallet wallet, String address) {
        Intent intent = new Intent(context, UpdateFriendActivity.class);
        intent.putExtra(C.Key.WALLET, wallet);
        intent.putExtra(C.EXTRA_ADDRESS, address);
        context.startActivityForResult(intent, requestCode);
    }
}

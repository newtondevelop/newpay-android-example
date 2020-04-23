package org.newtonproject.newtoncore.android.router;

import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.contact.AddFriendActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AddFriendRouter {
    public void open(Context context, Wallet wallet) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        intent.putExtra(C.Key.WALLET, wallet);
        context.startActivity(intent);
    }

    public void openWithAddress(Context context, Wallet wallet, String address) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        intent.putExtra(C.Key.WALLET, wallet);
        intent.putExtra(C.EXTRA_ADDRESS, address);
        context.startActivity(intent);
    }
}

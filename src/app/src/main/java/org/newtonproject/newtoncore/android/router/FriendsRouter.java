package org.newtonproject.newtoncore.android.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.contact.AddressBookActivity;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class FriendsRouter {
    public void openForResult(Activity context, Wallet wallet) {
        Intent intent = new Intent(context, AddressBookActivity.class);
        intent.putExtra(C.Key.WALLET, wallet);
        context.startActivityForResult(intent, C.REQUEST_CODE_FRIEND);
    }

    public void open(Context context,Wallet wallet, int source) {
        Intent intent = new Intent(context, AddressBookActivity.class);
        intent.putExtra(C.Key.WALLET, wallet);
        intent.putExtra(C.FRIEND_SOURCE, source);
        context.startActivity(intent);
    }
}

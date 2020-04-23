package org.newtonproject.newtoncore.android.router;

import android.app.Activity;
import android.content.Intent;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.views.account.ValidateBackupActivity;

import java.util.ArrayList;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ValidateBackupRouter {

    public void open(Activity context, boolean isClearStack, ArrayList<String> mnemonic, String password, String name) {
        Intent intent = new Intent(context, ValidateBackupActivity.class);
        if (isClearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        intent.putExtra(C.EXTRA_NAME, name);
        intent.putStringArrayListExtra(C.EXTRA_MNEMONIC, mnemonic);
        intent.putExtra(C.EXTRA_PASSWORD, password);
        context.startActivityForResult(intent, C.REQUEST_CODE_VALIDATE);
    }
}

package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.newtonproject.newtoncore.android.R;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class BackupView extends FrameLayout {
    private EditText password;

    public BackupView(@NonNull Context context) {
        super(context);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.layout_dialog_backup, this, true);
        password = findViewById(R.id.password);
    }

    public String getPassword() {
        return password.getText().toString();
    }

    public void showKeyBoard() {
        password.requestFocus();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showKeyBoard();
    }
}

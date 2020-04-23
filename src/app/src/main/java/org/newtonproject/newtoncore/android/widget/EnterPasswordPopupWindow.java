package org.newtonproject.newtoncore.android.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.widget.OnCompletePasswordListener;
import org.newtonproject.newtoncore.android.utils.KeyboardUtils;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class EnterPasswordPopupWindow extends PopupWindow implements View.OnClickListener {

    private final ViewGroup view;
    private EditText passwordEdittext;
    private TextView confirmButton;

    private OnCompletePasswordListener listener;

    private Handler handler = new Handler();

    public EnterPasswordPopupWindow(Context context, OnCompletePasswordListener onCompletePasswordListener) {
        LayoutInflater inflater = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (ViewGroup) inflater.inflate(R.layout.pop_enter_password, null);
        listener = onCompletePasswordListener;
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        initView();
    }

    private void initView() {
        passwordEdittext = view.findViewById(R.id.passwordEditText);
        passwordEdittext.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                onCompletePassword();
            }
            return true;
        });
        confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
        view.findViewById(R.id.closeButton).setOnClickListener(v -> dismiss());
        handler.postDelayed(() -> {
            KeyboardUtils.showKeyboard(passwordEdittext);
        }, 500);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton:
                onCompletePassword();
                break;
        }
    }

    private void onCompletePassword() {
        if(listener != null) {
            dismiss();
            listener.onPasswordComplete(passwordEdittext.getText().toString().trim());
        }
    }
}

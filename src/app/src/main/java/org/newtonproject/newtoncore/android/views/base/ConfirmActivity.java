package org.newtonproject.newtoncore.android.views.base;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/19--6:34 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ConfirmActivity extends BaseImplActivity {
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.contentTextView)
    TextView contentTextView;
    @BindView(R.id.confirmButton)
    Button confirmButton;
    @BindView(R.id.icon)
    ImageView imageView;

    @Override
    protected int getActivityTitle() {
        return R.string.confirm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_global_confirm;
    }

    @Override
    protected void initViewModel() {

    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(C.EXTRA_CONFIRM_TITLE);
        String content = intent.getStringExtra(C.EXTRA_CONFIRM_CONTENT);
        String buttonText = intent.getStringExtra(C.EXTRA_CONFIRM_BUTTON_TEXT);
        int imgRes = intent.getIntExtra(C.EXTRA_CONFIRM_ICON, -1);
        if(!TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
        }
        if(!TextUtils.isEmpty(buttonText)) {
            confirmButton.setText(buttonText);
        }
        if(imgRes != -1) {
            imageView.setImageResource(imgRes);
        }
        contentTextView.setText(content);
    }

    @OnClick(R.id.confirmButton)
    public void onViewClicked() {
        finish();
    }
}

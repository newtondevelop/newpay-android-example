package org.newtonproject.newtoncore.android.views.base;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.router.HomeRouter;
import org.newtonproject.newtoncore.android.views.account.BackupActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/19--6:34 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class CreateWalletSuccessActivity extends BaseImplActivity {
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.contentTextView)
    TextView contentTextView;
    @BindView(R.id.confirmButton)
    Button confirmButton;
    @BindView(R.id.icon)
    ImageView imageView;
    private static final int IMPORT_TYPE = 1;
    private static final int CREATE_TYPE = 2;
    private int CURRENT_TYPE = IMPORT_TYPE;

    @Override
    protected int getActivityTitle() {
        return R.string.confirm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_success;
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
            if(title.equals(getString(R.string.wallet_created))) {
                CURRENT_TYPE = CREATE_TYPE;
            } else {
                CURRENT_TYPE = IMPORT_TYPE;
            }
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
        switch (CURRENT_TYPE) {
            case CREATE_TYPE:
                Intent intent = new Intent(this, BackupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case IMPORT_TYPE:
                openHome();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openHome();
    }

    private void openHome() {
        new HomeRouter().open(this);
        finish();
    }

}

package org.newtonproject.newtoncore.android.views.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.router.AddFriendRouter;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.utils.StringUtil;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class PaySuccessActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PaySuccessActivity";
    private TextView accountTextView;
    private TextView addressTextView;
    private Button completeButton;
    private String receiveAddress;
    private String toAddress;
    private String amount;
    private RelativeLayout receiveRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        receiveAddress = getIntent().getStringExtra(C.EXTRA_ADDRESS);
        toAddress = getIntent().getStringExtra(C.EXTRA_TO_ADDRESS);
        amount = getIntent().getStringExtra(C.EXTRA_AMOUNT);
        initViews();
    }

    private void initViews() {
        accountTextView = findViewById(R.id.accountTextView);
        addressTextView = findViewById(R.id.addressTextView);
        completeButton = findViewById(R.id.completeButton);
        receiveRelativeLayout = findViewById(R.id.receiveRelativeLayout);
        accountTextView.setText(amount + " NEW");
        String contract = StringUtil.getContactName(toAddress, receiveAddress);
        addressTextView.setText(contract == null ? receiveAddress : String.format("(%s)%s", contract, receiveAddress));
        completeButton.setOnClickListener(this);
        receiveRelativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.completeButton:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.receiveRelativeLayout:
                new AddFriendRouter().openWithAddress(this, new Wallet(toAddress), receiveAddress);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}

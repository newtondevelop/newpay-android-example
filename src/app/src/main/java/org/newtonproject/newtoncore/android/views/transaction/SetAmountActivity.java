package org.newtonproject.newtoncore.android.views.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.views.widget.EditTextAmountWatcher;
import org.newtonproject.newtoncore.android.utils.BalanceUtils;
import org.newtonproject.newtoncore.android.utils.KeyboardUtils;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SetAmountActivity extends BaseActivity implements View.OnClickListener {

    private EditText amountEdittext;
    private Button confirmButton;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_amount);
        initView();
    }

    private void initView() {
        toolbar();
        amountEdittext = findViewById(R.id.amountEdittext);
        amountEdittext.addTextChangedListener(new EditTextAmountWatcher(amountEdittext));
        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(this);
        amountEdittext.requestFocus();
        handler.postDelayed(() -> {
            KeyboardUtils.showKeyboard(amountEdittext);
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_button:
                confirmAmount();
                break;
        }
    }

    private void confirmAmount() {
        String requestAmount = amountEdittext.getText().toString().trim();
        if(TextUtils.isEmpty(requestAmount)) {
            handler.postDelayed(() -> {
                KeyboardUtils.hideKeyboard(amountEdittext);
                setResult(RESULT_CANCELED);
                finish();
            }, 100);
            return;
        }
        String targetAmount = requestAmount.replace(",", "");
        if(isValidAmount(targetAmount)) {
            handler.postDelayed(() -> {
                KeyboardUtils.hideKeyboard(amountEdittext);
                Intent intent = new Intent();
                intent.putExtra(C.EXTRA_AMOUNT, targetAmount);
                setResult(RESULT_OK, intent);
                finish();
            }, 100);
        }else{
            Toast.makeText(this, getString(R.string.tip_invalid_amount), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.postDelayed(() -> {
            KeyboardUtils.hideKeyboard(amountEdittext);
            setResult(RESULT_CANCELED);
            finish();
        }, 10);
    }

    boolean isValidAmount(String eth) {
        try {
            String wei = BalanceUtils.EthToWei(eth);
            return wei != null;
        } catch (Exception e) {
            return false;
        }
    }
}

package org.newtonproject.newtoncore.android.views.contact;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UpdateWalletNameActivity extends BaseImplActivity implements View.OnClickListener {

    private EditText walletNameEdittext;
    private Button confirmButton;
    private String walletAddress;
    private SharedPreferenceRepository preferenceRepository;

    @Override
    protected int getActivityTitle() {
        return R.string.update_wallet_name;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_name;
    }

    @Override
    protected void initViewModel() {

    }

    protected void initView() {
        preferenceRepository = new SharedPreferenceRepository(this);
        walletAddress = getIntent().getStringExtra(C.EXTRA_ADDRESS);
        walletNameEdittext = findViewById(R. id.walletNameEdittext);
        walletNameEdittext.setText(preferenceRepository.getWalletName(walletAddress));
        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_button:
                String newName = walletNameEdittext.getText().toString().trim();
                if(!TextUtils.isEmpty(newName)) {
                    preferenceRepository.setWalletName(walletAddress, newName);
                    finish();
                } else {
                    Toast.makeText(this, R.string.wallet_name_required, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}

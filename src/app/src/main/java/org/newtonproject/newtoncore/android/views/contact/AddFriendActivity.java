package org.newtonproject.newtoncore.android.views.contact;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.data.entity.common.ScanResultInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.ContactsRepository;
import org.newtonproject.newtoncore.android.router.ScanRouter;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.utils.ScanUtils;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AddFriendActivity extends BaseImplActivity implements View.OnClickListener {

    private static final String TAG = "AddFriendActivity";
    private EditText nameEdittext;
    private EditText addressEdittext;
    private ImageButton scanImageButton;
    private Button confirmButton;
    private Wallet wallet;
    private String extraAddress;

    @Override
    protected int getActivityTitle() {
        return R.string.add_friend;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void initViewModel() {

    }

    protected void initView() {
        this.wallet = getIntent().getParcelableExtra(C.Key.WALLET);
        extraAddress = getIntent().getStringExtra(C.EXTRA_ADDRESS);
        nameEdittext = findViewById(R.id.nameEdittext);
        addressEdittext = findViewById(R.id.addressEdittext);
        scanImageButton = findViewById(R.id.scanImageButton);
        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(this);
        scanImageButton.setOnClickListener(this);

        if(extraAddress != null) {
            addressEdittext.setText(extraAddress);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanImageButton:
                boolean b = addressEdittext.requestFocus();
                scanAddress();
                break;
            case R.id.confirm_button:
                addFriend();
                break;
            default:
                break;
        }
    }

    private void scanAddress() {
        new ScanRouter().open(this);
    }

    private void addFriend() {
        String name = nameEdittext.getText().toString().trim();
        String address = addressEdittext.getText().toString().trim();
        if(TextUtils.isEmpty(address)) {
            Toast.makeText(this, getString(R.string.error_invalid_address), Toast.LENGTH_LONG).show();
            return;
        }
        if(!NewAddressUtils.checkNewAddress(address)) {
            Toast.makeText(this, getString(R.string.error_invalid_address), Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.friend_name_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if(address.equals(NewAddressUtils.hexAddress2NewAddress(wallet.address))) {
            Toast.makeText(this, R.string.friend_isnot_self, Toast.LENGTH_SHORT).show();
            return;
        }
        ContactsRepository c = new ContactsRepository();
        ContactsInfo info = new ContactsInfo(address, name);
        boolean b = c.addContact(wallet, info);
        if(b) {
            finish();
        }else {
            Toast.makeText(this,getString(R.string.contacts_exist), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(null != result) {
            String extracted_address = result.getContents();
            if(null != extracted_address) {
                ScanResultInfo info = ScanUtils.parseScanResult(extracted_address);
                if(info != null) {
                    addressEdittext.setText(info.address);
                } else {
                    Toast.makeText(this, getString(R.string.no_address_scaned), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, getString(R.string.no_address_scaned), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

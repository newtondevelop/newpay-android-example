package org.newtonproject.newtoncore.android.views.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.ContactsRepository;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UpdateFriendActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UpdateFriend";
    private ContactsRepository contactsRespository;

    private EditText friendNameEdittext;
    private TextView addressTextView;
    private Button confirmButton;
    private ContactsInfo contacts;
    private Wallet currentWallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_friend);
        toolbar();
        currentWallet = getIntent().getParcelableExtra(C.Key.WALLET);
        String address = getIntent().getStringExtra(C.EXTRA_ADDRESS);
        contactsRespository = new ContactsRepository();
        contacts = contactsRespository.getContacts(currentWallet, address);
        initView();
    }

    private void initView() {
        friendNameEdittext = findViewById(R.id.friendNameEdittext);
        addressTextView = findViewById(R.id.addressTextView);
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
        friendNameEdittext.setText(contacts.name);
        addressTextView.setText(contacts.address);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton:
                updateFriendAndFinish();
                break;
            default:
                break;
        }
    }

    private void updateFriendAndFinish() {
        String newName = friendNameEdittext.getText().toString().trim();
        if(TextUtils.isEmpty(newName)) {
            Toast.makeText(this, R.string.friend_name_required, Toast.LENGTH_SHORT).show();
            return;
        }
        String address = contacts.address;
        if(null != contactsRespository) {
            boolean b = contactsRespository.updateContact(currentWallet, address, newName);
            if(b) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}

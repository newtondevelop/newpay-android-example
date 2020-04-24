package org.newtonproject.newtoncore.android.views.transaction;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ErrorEnvelope;
import org.newtonproject.newtoncore.android.data.entity.common.GasSettings;
import org.newtonproject.newtoncore.android.data.entity.common.ScanResultInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.router.FriendsRouter;
import org.newtonproject.newtoncore.android.router.ManageWalletsRouter;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.views.widget.EditTextAmountWatcher;
import org.newtonproject.newtoncore.android.views.widget.OnCompletePasswordListener;
import org.newtonproject.newtoncore.android.utils.BalanceUtils;
import org.newtonproject.newtoncore.android.utils.KeyboardUtils;
import org.newtonproject.newtoncore.android.utils.MediaUtils;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.utils.ScanUtils;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.utils.Validators;
import org.newtonproject.newtoncore.android.utils.ViewUtils;
import org.newtonproject.newtoncore.android.viewmodels.GasSettingsViewModel;
import org.newtonproject.newtoncore.android.viewmodels.SendViewModel;
import org.newtonproject.newtoncore.android.viewmodels.SendViewModelFactory;
import org.newtonproject.newtoncore.android.widget.HelperEditTextLayout;
import org.newtonproject.newtoncore.android.widget.PayPopuwindow;
import org.newtonproject.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SendActivity extends BaseImplActivity<SendViewModel> implements OnCompletePasswordListener {

    private static final String TAG = "SendActivity";
    @Inject
    SendViewModelFactory sendViewModelFactory;

    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int GAS_FLAG_NORMAL = 1;
    private static final int GAS_FLAG_SPECIFIC = 2;
    private int GAS_FLAG = GAS_FLAG_NORMAL;
    @BindView(R.id.walletNameTextView)
    TextView walletNameTextView;
    @BindView(R.id.availableBalanceTextView)
    TextView availableBalanceTextView;
    @BindView(R.id.sentToLayout)
    HelperEditTextLayout sentToLayout;
    @BindView(R.id.amountLayout)
    HelperEditTextLayout amountLayout;
    @BindView(R.id.RemarkLayout)
    HelperEditTextLayout remarkLayout;
    @BindView(R.id.friendsButton)
    ImageView friendsButton;
    @BindView(R.id.nextButton)
    Button nextButton;
    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.sendAllTextView)
    Button sendAllTextView;


    // In case we're sending tokens
    private boolean sendingTokens = false;
    private String contractAddress;
    private int decimals;
    private String symbol;

    private Wallet wallet;
    private GasSettings gasSettings;
    private PayPopuwindow payPopuwindow;
    private AlertDialog dialog;
    private Handler handler = new Handler();
    private String to;
    private String amount;
    private String comment = null;
    private String requestPaySource;
    private String txid;
    private String mBalance;

    private static final int FOCUS_AMOUNT = 1;
    private static final int FOCUS_REMARK = 2;
    private static final int FOCUS_ADDRESS = 3;

    @Override
    protected int getActivityTitle() {
        return R.string.home_send;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sent;
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        detectOperation(intent);
        contractAddress = intent.getStringExtra(C.EXTRA_CONTRACT_ADDRESS);
        decimals = intent.getIntExtra(C.EXTRA_DECIMALS, C.ETHER_DECIMALS);
        symbol = intent.getStringExtra(C.EXTRA_SYMBOL);
        requestPaySource = intent.getStringExtra(C.EXTRA_PAY_REQUEST_SOURCE);
        symbol = symbol == null ? C.NEW_SYMBOL : symbol;
        sendingTokens = getIntent().getBooleanExtra(C.EXTRA_SENDING_TOKENS, false);
        // Populate to address if it has been passed forward
        String toAddress = getIntent().getStringExtra(C.EXTRA_ADDRESS);
        String amount = getIntent().getStringExtra(C.EXTRA_AMOUNT);
        amountLayout.addTextChangedListener(new EditTextAmountWatcher(amountLayout.getEditTextView()));
        if (toAddress != null) {
            sentToLayout.setText(NewAddressUtils.hexAddress2NewAddress(toAddress));
            if (!TextUtils.isEmpty(amount)) {
                amountLayout.setText(amount);
                amountLayout.setEditEnabled(false);
                showKeyBoard(FOCUS_REMARK);
            } else {
                amountLayout.setText(null);
                amountLayout.setEditEnabled(true);
                showKeyBoard(FOCUS_AMOUNT);
            }
        }

    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, sendViewModelFactory)
                  .get(SendViewModel.class);
        mViewModel.progress().observe(this, this::progress);
        mViewModel.error().observe(this, this::onError);
        mViewModel.defaultWallet().observe(this, this::onDefaultWallet);
        mViewModel.gasSettings().observe(this, this::onGasSettings);
        mViewModel.sendTransaction().observe(this, this::onTransaction);
        mViewModel.onGasError().observe(this, this::onGasError);
        mViewModel.onCreateTransactionError().observe(this, this::onCreateError);
        mViewModel.currentWalletBalance().observe(this, this::onBalance);
        mViewModel.onBalanceError().observe(this, this::onBalanceError);
        mViewModel.onCacheBalance().observe(this, this::onCacheBalance);

        mViewModel.prepare();
    }

    private void onCacheBalance(String s) {
        mBalance = s;
        if(TextUtils.isEmpty(mBalance)) return;
        String showBalance = String.format("%s %s", BalanceUtils.weiToNEW(new BigInteger(BalanceUtils.EthToWei(mBalance))), getString(R.string.big_new));
        availableBalanceTextView.setText(showBalance);
    }

    private void onBalanceError(Throwable throwable) {
        Toast.makeText(this, R.string.balance_error, Toast.LENGTH_SHORT).show();
        ViewUtils.buttonToggle(nextButton, true, R.string.pay);
    }

    private void onBalance(Map<String, String> balanceMap) {
        String balance = balanceMap.get(C.NEW_SYMBOL);
        mBalance = balance;
        Log.e(TAG, "Balance:" + balance);
        String showBalance = String.format("%s %s", BalanceUtils.weiToNEW(new BigInteger(BalanceUtils.EthToWei(balance))), getString(R.string.big_new));
        availableBalanceTextView.setText(showBalance);
        compareBalance(balanceMap.get(getString(R.string.big_new)));
    }

    private void compareBalance(String balance) {
        if(TextUtils.isEmpty(amount)) return;
        BigInteger amountInSubunits = BalanceUtils.baseToSubunit(amount, decimals);
        if (amountInSubunits.compareTo(new BigInteger(BalanceUtils.EthToWei(balance))) > 0) {
            Toast.makeText(this, R.string.insufficient_balance, Toast.LENGTH_SHORT).show();
            ViewUtils.buttonToggle(nextButton, true, R.string.pay);
            return;
        }
        GAS_FLAG = GAS_FLAG_NORMAL;
        if (TextUtils.isEmpty(comment)) {
            mViewModel.getGasSettings(wallet.address, NewAddressUtils.newAddress2HexAddress(to));
        } else {
            mViewModel.getGasSettings(wallet.address, NewAddressUtils.newAddress2HexAddress(to), amountInSubunits, comment);
        }
    }

    private void onCreateError(Throwable throwable) {
        ViewUtils.buttonToggle(nextButton, true, R.string.pay);
        hidePopWindow();
        Toast.makeText(this, null == throwable.getMessage() ? getString(R.string.transaction_error) : StringUtil.getTransactionError(throwable.getMessage()), Toast.LENGTH_LONG).show();
    }

    private void onGasError(Throwable throwable) {
        ViewUtils.buttonToggle(nextButton, true, R.string.pay);
        hidePopWindow();
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        ViewUtils.buttonToggle(nextButton, true, R.string.pay);
        hidePopWindow();
        if (wallet == null) {
            Toast.makeText(this, R.string.no_wallet, Toast.LENGTH_SHORT).show();
            new ManageWalletsRouter().open(this, true);
        }
    }

    private void onTransaction(String txidRes) {
        txid = txidRes;
        hidePopWindow();
        Intent intent = new Intent(this, PaySuccessActivity.class);
        intent.putExtra(C.EXTRA_AMOUNT, amountLayout.getEditText());
        intent.putExtra(C.EXTRA_ADDRESS, sentToLayout.getEditText());
        intent.putExtra(C.EXTRA_TO_ADDRESS, wallet.address);
        MediaUtils.startPlay(R.raw.send_voice);
        startActivityForResult(intent, C.REQUEST_CODE_PAY_SUCCESS);

    }

    private void hidePopWindow() {
        if (null != payPopuwindow && payPopuwindow.isShowing()) {
            payPopuwindow.dismiss();
            payPopuwindow = null;
        }
    }

    private void onGasSettings(GasSettings gasSettings) {
        if (gasSettings != null) {
            this.gasSettings = gasSettings;
            if(GAS_FLAG == GAS_FLAG_NORMAL) {
                payPopuwindow = null;
                KeyboardUtils.hideKeyboard(sentToLayout.getEditTextView());
                KeyboardUtils.hideKeyboard(amountLayout.getEditTextView());
                payPopuwindow = new PayPopuwindow(this, wallet.address, sentToLayout.getEditText(), amountLayout.getEditText(), remarkLayout.getEditText(), gasSettings, this);
                payPopuwindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                payPopuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        ViewUtils.buttonToggle(nextButton, true, R.string.pay);
                    }
                });
            } else {
                BigInteger fee = gasSettings.gasLimit.multiply(gasSettings.gasPrice);
                BigInteger maxAmount = BalanceUtils.baseToSubunit(mBalance, decimals).subtract(fee);
                String maxAmountStr = BalanceUtils.subunitToBase(maxAmount, decimals).toString();
                maxAmountStr = maxAmountStr.startsWith("-") ? "0" : maxAmountStr;
                amountLayout.setText(maxAmountStr);
            }

        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
            ViewUtils.buttonToggle(nextButton, true, R.string.pay);
        }
    }

    private void onDefaultWallet(Wallet wallet) {
        if (wallet == null) {
            Toast.makeText(this, R.string.no_wallet, Toast.LENGTH_SHORT).show();
            new ManageWalletsRouter().open(this, true);
        } else {
            this.wallet = wallet;
            walletNameTextView.setText(String.format("%s %s", StringUtil.getWalletName(mContext, wallet.address), getString(R.string.text_wallet_owner)));
            mViewModel.getCacheBalance(wallet);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan: {
                final String[] permissions = new String[]{Manifest.permission.CAMERA};
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                          Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
                } else {
                    mViewModel.openScan(this);
                }
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == C.REQUEST_CODE_FRIEND && resultCode == RESULT_OK) {
            String address = data.getStringExtra(C.EXTRA_ADDRESS);
            if (null != address) {
                sentToLayout.setText(address);
            }
            return;
        }
        if (requestCode == GasSettingsViewModel.SET_GAS_SETTINGS && resultCode == RESULT_OK) {
            if (payPopuwindow != null) {
                BigInteger gasPrice = new BigInteger(data.getStringExtra(C.EXTRA_GAS_PRICE));
                BigInteger gasLimit = new BigInteger(data.getStringExtra(C.EXTRA_GAS_LIMIT));
                GasSettings settings = new GasSettings(gasPrice, gasLimit);
                setGasSettingFormResult(settings);
            }
            return;
        }
        if (requestCode == C.REQUEST_CODE_PAY_SUCCESS && resultCode == RESULT_OK) {
            if (requestPaySource != null) {
                Intent intent = new Intent();
                intent.putExtra("txid", txid);
                intent.putExtra("ERROR_CODE", 1);
                setResult(RESULT_OK, intent);
            }
            finish();
            return;
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (null != result) {
            String resultContents = result.getContents();
            if (null != resultContents) {
                if(resultContents.startsWith("http://") || resultContents.startsWith("https://")) {
                    mViewModel.openWebView(mContext, null, resultContents);
                } else {
                    ScanResultInfo info = ScanUtils.parseScanResult(resultContents);
                    if (info != null) {
                        if (info.address != null) {
                            sentToLayout.setText(info.address);
                        }else{
                            Toast.makeText(this, R.string.no_address_scaned, Toast.LENGTH_SHORT).show();
                        }
                        if (!TextUtils.isEmpty(info.amount)) {
                            amountLayout.setText(info.amount);
                            amountLayout.setEditEnabled(false);
                            showKeyBoard(FOCUS_REMARK);
                        } else {
                            amountLayout.setText(null);
                            amountLayout.setEditEnabled(true);
                            showKeyBoard(FOCUS_AMOUNT);
                        }
                    } else {
                        Toast.makeText(this, R.string.no_address_scaned, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, R.string.no_address_scaned, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setGasSettingFormResult(GasSettings settings) {
        this.gasSettings = settings;
        if (null != payPopuwindow) {
            payPopuwindow.updateGasSettings(settings);
        }
    }

    private void onNext() {
        if (wallet == null || wallet.address == null) {
            Toast.makeText(this, R.string.no_wallet, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        to = sentToLayout.getEditText();
        amount = amountLayout.getEditText();
        comment = remarkLayout.getEditText();
        if (!NewAddressUtils.checkNewAddress(to)) {
            Toast.makeText(this, getString(R.string.tip_invalid_address), Toast.LENGTH_LONG).show();
            return;
        }
        if (!Validators.validateComment(comment)) {
            Toast.makeText(this, getString(R.string.comment_limit), Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidAmount(amount)) {
            Toast.makeText(this, getString(R.string.tip_invalid_amount), Toast.LENGTH_LONG).show();
            return;
        }
        BigInteger amountInSubunits = BalanceUtils.baseToSubunit(amount, decimals);
        if (amountInSubunits.compareTo(BigInteger.ZERO) <= 0) {
            Toast.makeText(this, R.string.min_transfer_amount, Toast.LENGTH_SHORT).show();
            return;
        }
        ViewUtils.buttonToggle(nextButton, false, R.string.sending_progress);
        compareBalance(mBalance);
    }


    boolean isValidAmount(String eth) {
        if (null == eth) return false;
        try {
            String wei = BalanceUtils.EthToWei(eth);
            return wei != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            //openScanActivity();
            mViewModel.openScan(this);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                  " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                  .setMessage(R.string.no_camera_permission)
                  .setPositiveButton(R.string.ok, listener)
                  .show();
    }

    @OnClick({R.id.nextButton, R.id.friendsButton, R.id.sendAllTextView})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                onNext();
                break;
            case R.id.friendsButton:
                new FriendsRouter().openForResult(this, new Wallet(new SharedPreferenceRepository(this).getCurrentWalletAddress()));
                break;
            case R.id.sendAllTextView:
                GAS_FLAG = GAS_FLAG_SPECIFIC;
                comment = remarkLayout.getEditText();
                if (TextUtils.isEmpty(comment)) {
                    mViewModel.getGasSettings(wallet.address, NewAddressUtils.newAddress2HexAddress(to));
                } else {
                    mViewModel.getGasSettings(wallet.address, NewAddressUtils.newAddress2HexAddress(to), BigInteger.ZERO, comment);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (requestPaySource != null) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onPasswordComplete(String password) {
        String amount = amountLayout.getEditText();
        String comment = remarkLayout.getEditText();
        BigInteger amountInSubunits = BalanceUtils.baseToSubunit(amount, decimals);
        if (amountInSubunits.compareTo(BigInteger.ZERO) > 0) {
            mViewModel.createTransaction(wallet.address, NewAddressUtils.newAddress2HexAddress(sentToLayout.getEditText()), amountInSubunits, gasSettings.gasPrice, gasSettings.gasLimit, password, TextUtils.isEmpty(comment) ? null : Numeric.hexStringToByteArray(Numeric.toHexString(comment.getBytes())));
        } else {
            Toast.makeText(this, R.string.min_transfer_amount, Toast.LENGTH_SHORT).show();
        }
    }

    private void showKeyBoard(int flag) {
        switch (flag) {
            case FOCUS_ADDRESS:
                handler.postDelayed(() -> {
                    sentToLayout.getEditTextView().requestFocus();
                    KeyboardUtils.showKeyboard(sentToLayout.getEditTextView());
                }, 500);
                break;
            case FOCUS_AMOUNT:
                handler.postDelayed(() -> {
                    amountLayout.getEditTextView().requestFocus();
                    KeyboardUtils.showKeyboard(amountLayout.getEditTextView());
                }, 500);
                break;
            case FOCUS_REMARK:
                handler.postDelayed(() -> {
                    remarkLayout.getEditTextView().requestFocus();
                    KeyboardUtils.showKeyboard(remarkLayout.getEditTextView());
                }, 500);
                break;
        }

    }

    @Override
    public void onNewIntent(Intent intent) {
        detectOperation(intent);
    }

    public void detectOperation(Intent intent) {
        Tag detectedTag;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef.get(detectedTag);
            String tagText = readNfcTag(intent);
            sentToLayout.setText(NewAddressUtils.hexAddress2NewAddress(tagText.trim()));
        }
    }


    private String readNfcTag(Intent intent) {
        String mTagText = "";
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                      NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    String textRecord = parseTextRecord(record);
                    mTagText = textRecord;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mTagText;
    }

    public static String parseTextRecord(NdefRecord ndefRecord) {
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0x3f;
            String textRecord = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}

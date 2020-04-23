package org.newtonproject.newtoncore.android.views.transaction;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.router.SetAmountRouter;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.utils.ViewUtils;

import butterknife.BindView;

import static org.newtonproject.newtoncore.android.C.Key.WALLET;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class MyAddressActivity extends BaseImplActivity implements View.OnClickListener {

    private static final float QR_IMAGE_WIDTH_RATIO = 2.0f;
    public static final String KEY_ADDRESS = "key_address";
    private static final String TAG = "MyAddress";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.centerTitle)
    TextView centerTitle;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.qr_image)
    ImageView qrImageView;
    @BindView(R.id.amountTextView)
    TextView amountTextView;
    @BindView(R.id.setAmountButton)
    TextView setAmountButton;
    @BindView(R.id.copy_action)
    TextView copyAction;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.walletNameTextView)
    TextView walletNameTextView;
    @BindView(R.id.shareTipTextView)
    TextView shareTipTextView;


    private Wallet wallet;
    private Bitmap addressQrCodeBitmap;
    private int screen_brightness;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        address.setDrawingCacheEnabled(true);
    }

    private Bitmap createQRImage(String qrContent) {
        return ViewUtils.createQRImage(this, qrContent, QR_IMAGE_WIDTH_RATIO);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        recoverScreenLight();
    }

    @Override
    protected int getActivityTitle() {
        return R.string.home_receive;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_address;
    }

    @Override
    protected void initViewModel() {
        recordScreenLight();
        wallet = getIntent().getParcelableExtra(WALLET);
        address.setText(NewAddressUtils.hexAddress2NewAddress(wallet.address));
        walletNameTextView.setText(String.format("%s %s", StringUtil.getWalletName(mContext, wallet.address), getString(R.string.text_wallet_owner)));
        copyAction.setOnClickListener(this);
        qrImageView.setOnClickListener(this);
        addressQrCodeBitmap = createQRImage(NewAddressUtils.hexAddress2NewAddress(wallet.address));
        qrImageView.setImageBitmap(addressQrCodeBitmap);
        setAmountButton.setOnClickListener(this);
    }

    private void recordScreenLight() {
        try {
            screen_brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            setScreenBrightness(255);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setScreenBrightness(int value) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = value * (1f/255f);
        getWindow().setAttributes(lp);
    }

    private void recoverScreenLight() {
        setScreenBrightness(screen_brightness);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copy_action:
            case R.id.qr_image:
                copyAddress();
                break;
            case R.id.setAmountButton:
                if (amountTextView.getVisibility() == View.GONE) {
                    setAmount();
                } else {
                    clearAmount();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            checkPermissionForApp();
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareAddressImage() {
        shareTipTextView.setVisibility(View.VISIBLE);
        Bitmap viewBitmap = ViewUtils.getViewBitmap(cardView);
        String urlString = MediaStore.Images.Media.insertImage(getContentResolver(), viewBitmap, getString(R.string.address_title), wallet.address);
        Uri uri = Uri.parse(urlString);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent = Intent.createChooser(intent, getString(R.string.share_via));
        startActivity(intent);
    }

    private void clearAmount() {
        setAmountCancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.REQUEST_CODE_SET_AMOUNT && resultCode == RESULT_OK) {
            String amount = data.getStringExtra(C.EXTRA_AMOUNT);
            updateQrCode(amount);
        }
        if (requestCode == C.REQUEST_CODE_SET_AMOUNT && resultCode == RESULT_CANCELED) {
            setAmountCancel();
        }
    }

    private void setAmountCancel() {
        amountTextView.setText(null);
        qrImageView.setImageBitmap(addressQrCodeBitmap);
        setAmountButton.setText(R.string.set_account);
        amountTextView.setVisibility(View.GONE);
    }

    private void checkPermissionForApp() {
        int requestWriteState = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (requestWriteState != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, C.PERMISSION_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            shareAddressImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case C.PERMISSION_CODE_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    shareAddressImage();
                } else {
                    Toast.makeText(this, R.string.write_external_permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        shareTipTextView.setVisibility(View.GONE);
    }

    private void updateQrCode(String amount) {
        String content = NewAddressUtils.generateQrString(wallet.address, amount);
        Bitmap bitmap = createQRImage(content);
        qrImageView.setImageBitmap(bitmap);
        amountTextView.setText(String.format("%s NEW", amount));
        amountTextView.setVisibility(View.VISIBLE);
    }

    private void setAmount() {
        new SetAmountRouter().openForResult(this);
        setAmountButton.setText(R.string.clear_amount);
    }

    private void copyAddress() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(KEY_ADDRESS, NewAddressUtils.hexAddress2NewAddress(wallet.address));
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(this, getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show();
    }
}

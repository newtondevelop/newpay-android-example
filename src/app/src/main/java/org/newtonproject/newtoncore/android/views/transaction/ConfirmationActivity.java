package org.newtonproject.newtoncore.android.views.transaction;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ErrorEnvelope;
import org.newtonproject.newtoncore.android.data.entity.common.GasSettings;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.utils.BalanceUtils;
import org.newtonproject.newtoncore.android.viewmodels.ConfirmationViewModel;
import org.newtonproject.newtoncore.android.viewmodels.ConfirmationViewModelFactory;
import org.newtonproject.newtoncore.android.viewmodels.GasSettingsViewModel;
import org.newtonproject.newtoncore.android.widget.EnterPasswordView;

import java.math.BigInteger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ConfirmationActivity extends BaseActivity {
    AlertDialog dialog;

    @Inject
    ConfirmationViewModelFactory confirmationViewModelFactory;
    ConfirmationViewModel viewModel;

    private TextView fromAddressText;
    private TextView toAddressText;
    private TextView valueText;
    private TextView gasPriceText;
    private TextView gasLimitText;
    private TextView networkFeeText;
    private Button sendButton;

    private BigInteger amount;
    private int decimals;
    private String contractAddress;
    private boolean confirmationForTokenTransfer = false;
    private LinearLayout rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm);
        toolbar();
        rootView = findViewById(R.id.rootView);
        fromAddressText = findViewById(R.id.text_from);
        toAddressText = findViewById(R.id.text_to);
        valueText = findViewById(R.id.text_value);
        gasPriceText = findViewById(R.id.text_gas_price);
        gasLimitText = findViewById(R.id.text_gas_limit);
        networkFeeText = findViewById(R.id.text_network_fee);
        sendButton = findViewById(R.id.send_button);

        sendButton.setOnClickListener(view -> onSend());

        String toAddress = getIntent().getStringExtra(C.EXTRA_TO_ADDRESS);
        contractAddress = getIntent().getStringExtra(C.EXTRA_CONTRACT_ADDRESS);
        amount = new BigInteger(getIntent().getStringExtra(C.EXTRA_AMOUNT));
        decimals = getIntent().getIntExtra(C.EXTRA_DECIMALS, -1);
        String symbol = getIntent().getStringExtra(C.EXTRA_SYMBOL);
        symbol = symbol == null ? C.NEW_SYMBOL : symbol;

        confirmationForTokenTransfer = contractAddress != null;

        toAddressText.setText(toAddress);

        String amountString = "-" + BalanceUtils.subunitToBase(amount, decimals).toPlainString() + " " + symbol;
        valueText.setText(amountString);
        valueText.setTextColor(ContextCompat.getColor(this, R.color.red));

        viewModel = ViewModelProviders.of(this, confirmationViewModelFactory)
                .get(ConfirmationViewModel.class);

        viewModel.defaultWallet().observe(this, this::onDefaultWallet);
        viewModel.gasSettings().observe(this, this::onGasSettings);
        viewModel.sendTransaction().observe(this, this::onTransaction);
        viewModel.progress().observe(this, this::onProgress);
        viewModel.error().observe(this, this::onError);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirmation_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                viewModel.openGasSettings(ConfirmationActivity.this);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.prepare(confirmationForTokenTransfer);
    }

    private void onProgress(boolean shouldShowProgress) {
        hideDialog();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog_sending)
                    .setView(new ProgressBar(this))
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void onSend() {
        GasSettings gasSettings = viewModel.gasSettings().getValue();
        EnterPasswordView view = new EnterPasswordView(this);
        if (!confirmationForTokenTransfer) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.enter_transaction_password))
                    .setView(view)
                    .setMessage(getString(R.string.transaction_password_tip))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.createTransaction(
                                    fromAddressText.getText().toString(),
                                    toAddressText.getText().toString(),
                                    amount,
                                    gasSettings.gasPrice,
                                    gasSettings.gasLimit,
                                    view.getPassword());
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .create();
            dialog.show();
        } else {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.enter_transaction_password))
                    .setView(view)
                    .setMessage(getString(R.string.transaction_password_tip))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.createTokenTransfer(
                                    fromAddressText.getText().toString(),
                                    toAddressText.getText().toString(),
                                    contractAddress,
                                    amount,
                                    gasSettings.gasPrice,
                                    gasSettings.gasLimit,
                                    view.getPassword());
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .create();
            dialog.show();
        }
    }

    private void onDefaultWallet(Wallet wallet) {
        fromAddressText.setText(wallet.address);
    }

    private void onTransaction(String hash) {
        hideDialog();
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.transaction_succeeded)
                .setMessage(hash)
                .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                    finish();
                })
                .setNeutralButton(R.string.copy, (dialog1, id) -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("transaction hash", hash);
                    clipboard.setPrimaryClip(clip);
                    finish();
                })
                .create();
        dialog.show();
    }

    private void onGasSettings(GasSettings gasSettings) {
        String gasPrice = BalanceUtils.weiToGwei(gasSettings.gasPrice) + " " + C.GWEI_UNIT;
        gasPriceText.setText(gasPrice);
        gasLimitText.setText(gasSettings.gasLimit.toString());

        String networkFee = BalanceUtils.weiToEth(gasSettings
                .gasPrice.multiply(gasSettings.gasLimit)).toPlainString() + " " + C.NEW_SYMBOL;
        networkFeeText.setText(networkFee);
    }

    private void onError(ErrorEnvelope error) {
        hideDialog();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.error_transaction_failed)
                .setMessage(error.message)
                .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                    // Do nothing
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == GasSettingsViewModel.SET_GAS_SETTINGS) {
            if (resultCode == RESULT_OK) {
                BigInteger gasPrice = new BigInteger(intent.getStringExtra(C.EXTRA_GAS_PRICE));
                BigInteger gasLimit = new BigInteger(intent.getStringExtra(C.EXTRA_GAS_LIMIT));
                GasSettings settings = new GasSettings(gasPrice, gasLimit);
                viewModel.onGasSettings(settings);
            }
        }
    }
}

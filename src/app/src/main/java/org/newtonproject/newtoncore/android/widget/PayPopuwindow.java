package org.newtonproject.newtoncore.android.widget;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.GasSettings;
import org.newtonproject.newtoncore.android.views.widget.OnCompletePasswordListener;
import org.newtonproject.newtoncore.android.utils.KeyboardUtils;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.utils.ViewUtils;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class PayPopuwindow extends PopupWindow implements View.OnClickListener {

    private static final String TAG = "PayPopuwindow";
    private Context context;
    private final ViewGroup view;
    private ConstraintLayout confirmLayout;
    private ConstraintLayout sendTransactionLayout;
    private Button enterPasswordButton;
    private TextView amountTextView;

    private String fromAddress;
    private String toAddress;
    private String amount;
    private String comment;
    private GasSettings gasSettings;
    private TextView fromAddressTextView;
    private TextView toAddressTextView;
    private TextView gasPriceTextView;
    private TextView gasLimitTextView;
    private TextView gasFeeTextView;

    private OnCompletePasswordListener onCompletePasswordListener;
    private EditText passwordEditText;
    private ImageButton closeBt;
    private ImageButton closeButton;
    private ConstraintLayout rootview;
    private TextView commentTextView;
    private LinearLayout commentLayout;
    private TextView confirmButton;

    public PayPopuwindow(Context context, String address, String to, String amount, GasSettings gasSettings, OnCompletePasswordListener onCompletePasswordListener) {
        this(context, address, to, amount,null, gasSettings, onCompletePasswordListener);

    }

    public PayPopuwindow(Context context, String address, String to, String amount,String comment,GasSettings gasSettings, OnCompletePasswordListener onCompletePasswordListener) {
        LayoutInflater inflater = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (ViewGroup) inflater.inflate(R.layout.popuwindow_setpassword, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setFocusable(true);
        initView();
        if(!TextUtils.isEmpty(comment)){
            this.comment = comment;
            commentLayout.setVisibility(View.VISIBLE);
        }
        this.context = context;
        this.fromAddress = address;
        this.toAddress = to;
        this.amount = amount;
        this.gasSettings = gasSettings;
        this.onCompletePasswordListener = onCompletePasswordListener;
        fillView();
    }
    private void initView() {
        confirmLayout = view.findViewById(R.id.confirmLayout);
        sendTransactionLayout = view.findViewById(R.id.signLayout);
        enterPasswordButton = view.findViewById(R.id.enterPasswordButton);
        closeBt = view.findViewById(R.id.closeBt);
        closeButton = view.findViewById(R.id.closeButton);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        amountTextView = view.findViewById(R.id.amountTextView);
        fromAddressTextView = view.findViewById(R.id.fromAddressTextView);
        toAddressTextView = view.findViewById(R.id.toAddressTextView);
        gasPriceTextView = view.findViewById(R.id.gasPriceTextView);
        gasLimitTextView = view.findViewById(R.id.gasLimitTextView);
        gasFeeTextView = view.findViewById(R.id.gasFeeTextView);
        rootview = view.findViewById(R.id.rootView);
        commentLayout = view.findViewById(R.id.commentLayout);
        commentTextView = view.findViewById(R.id.commentTextview);
        confirmButton = view.findViewById(R.id.confirmButton);
        enterPasswordButton.setOnClickListener(this);
        closeBt.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                if(onCompletePasswordListener != null) {
                    dismiss();
                    onCompletePasswordListener.onPasswordComplete(passwordEditText.getText().toString().trim());
                }
            }
            return true;
        });
    }

    private void fillView() {
        amountTextView.setText(amount + " " + C.NEW_SYMBOL);
        fromAddressTextView.setText(NewAddressUtils.hexAddress2NewAddress(fromAddress));
        toAddressTextView.setText(toAddress);
        //gasPriceTextView.setText(BalanceUtils.weiToGwei(gasSettings.gasPrice) + " " + C.GWEI_UNIT);
        if(gasSettings != null) {
            gasPriceTextView.setText(gasSettings.gasPrice + " " + C.ISSAC_UNIT);
            gasLimitTextView.setText(gasSettings.gasLimit.toString());
            //String networkFee = BalanceUtils.weiToEth(gasSettings.gasPrice.multiply(gasSettings.gasLimit)).toPlainString() + " " + C.NEW_SYMBOL;
            String networkFee = gasSettings.gasPrice.multiply(gasSettings.gasLimit) + " " + C.ISSAC_UNIT;
            gasFeeTextView.setText(networkFee);
        }
        commentTextView.setText(comment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enterPasswordButton:
                confirmLayout.startAnimation(ViewUtils.leaveAnimation());
                confirmLayout.setVisibility(View.GONE);
                ViewUtils.buttonToggle(enterPasswordButton, false, null);
                TranslateAnimation translateAnimation = ViewUtils.enterAnimation();
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        KeyboardUtils.showKeyboard(passwordEditText);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                sendTransactionLayout.startAnimation(translateAnimation);
                sendTransactionLayout.setVisibility(View.VISIBLE);
                boolean b = passwordEditText.requestFocus();
                break;
            case R.id.closeBt:
            case R.id.closeButton:
                KeyboardUtils.hideKeyboard(passwordEditText);
                this.dismiss();
                break;
            case R.id.confirmButton:
                if(onCompletePasswordListener != null) {
                    dismiss();
                    onCompletePasswordListener.onPasswordComplete(passwordEditText.getText().toString().trim());
                }
                break;
            default:
                break;
        }
    }

    public void updateGasSettings(GasSettings gasSettings) {
        this.gasSettings = gasSettings;
        fillView();
    }
}

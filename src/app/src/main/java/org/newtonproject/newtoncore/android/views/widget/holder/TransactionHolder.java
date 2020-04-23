package org.newtonproject.newtoncore.android.views.widget.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionOperation;
import org.newtonproject.newtoncore.android.views.widget.OnTransactionClickListener;
import org.newtonproject.newtoncore.android.utils.BalanceUtils;
import org.newtonproject.newtoncore.android.utils.CheckUtils;
import org.newtonproject.newtoncore.android.utils.DateUtils;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.utils.ViewUtils;

import java.math.BigInteger;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionHolder extends BinderViewHolder<Transaction> implements View.OnClickListener {

    public static final int VIEW_TYPE = 1003;

    private static final int SIGNIFICANT_FIGURES = 3;

    public static final String DEFAULT_ADDRESS_ADDITIONAL = "default_address";
    public static final String DEFAULT_SYMBOL_ADDITIONAL = "network_symbol";
    private static final String TAG = "TransactionHolder";

    private final TextView time;
    private final TextView address;
    private final TextView value;
    private final ImageView typeIcon;

    private Transaction transaction;
    private String defaultAddress;
    private OnTransactionClickListener onTransactionClickListener;

    public TransactionHolder(int resId, ViewGroup parent) {
        super(resId, parent);

        typeIcon = findViewById(R.id.type_icon);
        address = findViewById(R.id.address);
        time = findViewById(R.id.timeTextView);
        value = findViewById(R.id.value);

        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(@Nullable Transaction data, @NonNull Bundle addition) {
        transaction = data; // reset
        if (this.transaction == null) {
            return;
        }
        defaultAddress = addition.getString(DEFAULT_ADDRESS_ADDITIONAL);

        String networkSymbol = addition.getString(DEFAULT_SYMBOL_ADDITIONAL);
        // If operations include token transfer, display token transfer instead
        TransactionOperation operation = transaction.operations == null
                || transaction.operations.length == 0 ? null : transaction.operations[0];
        fill();
    }

    private void fill() {
        boolean isSent = transaction.from.toLowerCase().equals(defaultAddress);
        int transactionStatus = CheckUtils.checkTransactionStatus(defaultAddress, transaction.from.toLowerCase(), transaction.to.toLowerCase());
        if (!TextUtils.isEmpty(transaction.error)) {
            typeIcon.setImageResource(R.drawable.ic_error_outline_black_24dp);
        } else{
            typeIcon.setImageResource(ViewUtils.getTransactionStatusIcon(transactionStatus));
        }
        String addressText = isSent ? NewAddressUtils.hexAddress2NewAddress(transaction.to) : NewAddressUtils.hexAddress2NewAddress(transaction.from);
        String name = StringUtil.getContactName(defaultAddress, addressText);
        address.setText(name == null ? addressText : name);
        time.setText(DateUtils.getHourTime(transaction.timeStamp));
        String valueStr = transaction.value;
        String res = BalanceUtils.weiToNEW(new BigInteger(valueStr)) + " " + C.NEW_SYMBOL;
        this.value.setText(res);
    }

    @Override
    public void onClick(View view) {
        if (onTransactionClickListener != null) {
            onTransactionClickListener.onTransactionClick(view, transaction);
        }
    }

    public void setOnTransactionClickListener(OnTransactionClickListener onTransactionClickListener) {
        this.onTransactionClickListener = onTransactionClickListener;
    }
}

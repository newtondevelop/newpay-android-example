package org.newtonproject.newtoncore.android.views.transaction;

import android.arch.lifecycle.ViewModelProviders;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.utils.BalanceUtils;
import org.newtonproject.newtoncore.android.utils.DateUtils;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.viewmodels.TransactionDetailViewModel;
import org.newtonproject.newtoncore.android.viewmodels.TransactionDetailViewModelFactory;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

import static org.newtonproject.newtoncore.android.C.Key.TRANSACTION;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionDetailActivity extends BaseImplActivity<TransactionDetailViewModel> {

    @Inject
    TransactionDetailViewModelFactory transactionDetailViewModelFactory;

    @BindView(R.id.amountTextView)
    TextView amountTextView;
    @BindView(R.id.fee)
    TextView fee;
    @BindView(R.id.fromAddressTextView)
    TextView fromAddressTextView;
    @BindView(R.id.toAddressTextView)
    TextView toAddressTextView;
    @BindView(R.id.noteTextView)
    TextView noteTextView;
    @BindView(R.id.txidTextView)
    TextView txidTextView;
    @BindView(R.id.timeTextView)
    TextView timeTextView;
    @BindView(R.id.more_detail)
    Button moreDetail;

    private Transaction transaction;
    private String txid;
    private Wallet currentWallet;

    @Override
    protected int getActivityTitle() {
        return -1;
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_detail;
    }

    @Override
    protected void initView() {
        transaction = getIntent().getParcelableExtra(TRANSACTION);
        txid = getIntent().getStringExtra(C.EXTRA_TXID);
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, transactionDetailViewModelFactory).get(TransactionDetailViewModel.class);
        mViewModel.defaultNetwork().observe(this, this::onDefaultNetwork);
        mViewModel.defaultWallet().observe(this, this::onDefaultWallet);
        mViewModel.currentTransaction().observe(this, this::onTransaction);
        mViewModel.onCommonError().observe(this, this::onCommonError);
        mViewModel.progress().observe(this, this::progress);
    }

    private void fillView() {
        BigInteger gasFee = new BigInteger(transaction.gasUsed).multiply(new BigInteger(transaction.gasPrice));
        String fromText = NewAddressUtils.hexAddress2NewAddress(transaction.from);
        String fromName = StringUtil.getContactName(currentWallet.address, fromText);

        String toText = NewAddressUtils.hexAddress2NewAddress(transaction.to);
        String toName = StringUtil.getContactName(currentWallet.address, toText);
        // comment
        String comment = new String(Numeric.hexStringToByteArray(transaction.input));
        noteTextView.setText(TextUtils.isEmpty(comment) ? "-" : comment);
        // fee
        fee.setText(String.format(getString(R.string.text_tx_detail_fee), gasFee.toString()));
        // from
        fromAddressTextView.setText(fromName == null ? fromText : String.format("(%s) %s", fromName, fromText));
        // to
        toAddressTextView.setText(toName == null ? toText : String.format("(%s) %s", toName, toText));
        // txid
        txidTextView.setText(transaction.hash);
        // time
        timeTextView.setText(DateUtils.getDateTime(transaction.timeStamp));
        // amount
        amountTextView.setText(String.format("%s %s",BalanceUtils.weiToNEW(new BigInteger(transaction.value)), C.NEW_SYMBOL));
    }


    private void onTransaction(Transaction transaction) {
        this.transaction = transaction;
        fillView();
        if (transaction == null) {
            finish();
        }
    }

    private void onDefaultWallet(Wallet wallet) {
        this.currentWallet = wallet;

        if (transaction != null) {
            fillView();
        } else {
            if (txid == null) {
                finish();
            }else{
                mViewModel.findTransaction(wallet, txid);
            }
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
            mViewModel.shareTransactionDetail(this, transaction);
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDefaultNetwork(NetworkInfo networkInfo) {
        findViewById(R.id.more_detail).setVisibility(
                TextUtils.isEmpty(networkInfo.explorerUrl) ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.more_detail})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_detail:
                if(transaction == null) return;
                mViewModel.showMoreDetails(v.getContext(), transaction);
                break;
        }
    }
}

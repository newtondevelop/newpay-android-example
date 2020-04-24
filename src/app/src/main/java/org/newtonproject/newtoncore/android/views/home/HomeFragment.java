package org.newtonproject.newtoncore.android.views.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.EventMessage;
import org.newtonproject.newtoncore.android.data.entity.common.ScanResultInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.entity.hepnode.NewNetAuth;
import org.newtonproject.newtoncore.android.data.entity.hepnode.NewNetPay;
import org.newtonproject.newtoncore.android.data.entity.hepnode.NewNetProofSubmit;
import org.newtonproject.newtoncore.android.data.entity.response.AssetInfo;
import org.newtonproject.newtoncore.android.data.entity.response.MarketToken;
import org.newtonproject.newtoncore.android.data.entity.response.ProfileInfo;
import org.newtonproject.newtoncore.android.data.manager.AccountManager;
import org.newtonproject.newtoncore.android.data.manager.ProfileManager;
import org.newtonproject.newtoncore.android.router.TransactionsRouter;
import org.newtonproject.newtoncore.android.utils.BalanceUtils;
import org.newtonproject.newtoncore.android.utils.ScanUtils;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.utils.ToastUtils;
import org.newtonproject.newtoncore.android.utils.ViewUtils;
import org.newtonproject.newtoncore.android.viewmodels.home.HomeModel;
import org.newtonproject.newtoncore.android.viewmodels.home.HomeViewModelFactory;
import org.newtonproject.newtoncore.android.views.base.BaseFragment;
import org.newtonproject.newtoncore.android.views.widget.OnCompletePasswordListener;
import org.newtonproject.newtoncore.android.widget.CheckDialog;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class HomeFragment extends BaseFragment<HomeModel> implements OnCompletePasswordListener {

    private static final String TAG = "HomeFragment";
    @Inject
    HomeViewModelFactory homeViewModelFactory;

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.backgroundImage)
    ImageView backgroundImageView;
    @BindView(R.id.homeCard)
    CardView homeCard;
    // balance summary
    @BindView(R.id.summaryBalanceCardView)
    LinearLayout summaryBalanceCardView;
    @BindView(R.id.summaryTotalBalanceTextView)
    TextView summaryTotalBalanceTextView;
    @BindView(R.id.summaryWalletNameTextView)
    TextView summaryWalletNameTextView;
    // action layout
    @BindView(R.id.scanLinearLayout)
    LinearLayout scanLinearLayout;
    @BindView(R.id.receiveLinearLayout)
    LinearLayout receiveLinearLayout;
    @BindView(R.id.sendLinearLayout)
    LinearLayout sendLinearLayout;

    // test tip
    @BindView(R.id.fraudTipLayout)
    LinearLayout fraudTipLayout;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private Wallet currentWallet;
    private String mTotalBalance;
    private ProfileInfo mProfileInfo;
    private String signNonceMessage;
    private String availableBalance;
    private AssetInfo assetInfo;
    private String sigmessage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected int getTitle() {
        return -1;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.onCleared();
        mViewModel.checkPing(mContext);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void initViews() {
        BitmapDrawable blurBackground = ViewUtils.getBlurBackground();
        toolbar.setBackground(blurBackground);
        swipeRefreshLayout.setOnRefreshListener(this::initData);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 200);
    }

    private void initData() {
        initWithWallet();
        initRequest();
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, homeViewModelFactory).get(HomeModel.class);
        // get balance
        mViewModel.cacheBalance().observe(this, this::onCacheBalance);
        mViewModel.currentWalletBalance().observe(this, this::onCurrentWalletBalance);
        // sign nonce message
//        mViewModel.onSignNonceMessage().observe(this, this::onSignNonceMessage);
        // get assert info
        mViewModel.onAssertInfo().observe(this, this::onAssertInfo);
        // common error
        mViewModel.onCommonError().observe(this, this::onCommonError);
        // on total balance
        mViewModel.onTotalCacheBalance().observe(this, this::onTotalCacheBalance);
        mViewModel.progress().observe(this, this::progress);
        // is need back up wallet
        mViewModel.isNeedBackUpWallet().observe(this, this::isNeedBackUpWallet);
        // on progress with show progress dialog
        mViewModel.onProgress().observe(this, this::onProgress);
        // on auth
        mViewModel.onNewNetAuth().observe(this, this::onNewNetAuth);
        mViewModel.onNewNetPay().observe(this, this::onNewNetPay);
        mViewModel.onNewNetProofSubmit().observe(this, this::onNewNetProofSubmit);
        if (C.APP_ENV.equals("product")) {
            fraudTipLayout.setVisibility(View.GONE);
        }
        mViewModel.checkNetWork();
        currentWallet = AccountManager.getInstance().getDefaultWallet();
        mViewModel.checkPing(mContext);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if(mProfileInfo == null && currentWallet != null) {
                mProfileInfo = ProfileManager.getInstance().profileInfo();
                if(mProfileInfo != null) {
                    onLocalProfile(mProfileInfo);
                }
            }
        }
    }

    private void isNeedBackUpWallet(Boolean aBoolean) {
        if(aBoolean) {
            CheckDialog dialog = new CheckDialog();
            dialog.setMargin(16);
            dialog.setNeedChecked(false);
            dialog.setTitle(R.string.backup_wallet);
            dialog.setContinueText(R.string.backup_wallet);
            dialog.setOutCancel(true);
            ArrayList<String> list = new ArrayList<>();
            list.add(getString(R.string.tip_backup_wallet_1));
            list.add(getString(R.string.tip_backup_wallet_2));
            dialog.setData(list);
            dialog.setOnContinueListener(()-> mViewModel.openBackUp(mContext));
            dialog.show(getActivity().getSupportFragmentManager());
        }
    }

    private void onTotalCacheBalance(String s) {
        if(TextUtils.equals("0", s)) {
            String showBalance = String.format("%s %s", StringUtil.formatAmount(BalanceUtils.getStandardNewForShow(availableBalance)), getString(R.string.big_new));
            summaryTotalBalanceTextView.setText(showBalance);
        }else{
            summaryTotalBalanceTextView.setText(String.format("%s %s", StringUtil.formatAmount(s), C.NEW_SYMBOL));
        }
    }

    private void progress(Boolean aBoolean) {
        if(!aBoolean) {
            swipeRefreshLayout.setRefreshing(false);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    protected void onCommonError(Throwable throwable) {
        ToastUtils.showNormalToast(mContext, throwable.getMessage());
    }

    private void initWithWallet() {
        String walletName = mViewModel.getWalletName(currentWallet.address);
        mViewModel.getBalance(currentWallet);
        summaryWalletNameTextView.setText(String.format("%s", walletName));
//        mViewModel.getLocalProfile(currentWallet);
    }

    private void initRequest() {
        mViewModel.setDefaultWallet(currentWallet);
        // get cache balance
        mViewModel.getCacheBalance(currentWallet);
        mViewModel.getTotalCahceBalance(currentWallet);
        if(C.IS_CHECK_BACKUP) {
            mViewModel.checkHasBackUpWallet();
            C.IS_CHECK_BACKUP = false;
        }
        progress(false);
    }

    // get local profile and init profile manager.
    private void onLocalProfile(ProfileInfo profileInfo) {
        if (profileInfo != null) {
            // has new id, init status
            ProfileManager.getInstance().updateProfile(profileInfo);
            mProfileInfo = profileInfo;
            preSignNonceForAsset();
        }
    }

    public void preSignNonceForAsset() {
        signNonceMessage = StringUtil.getNonce();
        mViewModel.signMessageWithKey(mProfileInfo.accessKey, signNonceMessage);
    }

    private void onCacheBalance(String balanceStr) {
        if (null != balanceStr) {
            availableBalance = balanceStr;
            String showBalance = String.format("%s %s", StringUtil.formatAmount(BalanceUtils.getStandardNewForShow(balanceStr)), getString(R.string.big_new));
            setAvailableBalanceTextView(showBalance);
        }
    }

    private void onAssertInfo(AssetInfo assetInfo) {
        this.assetInfo = assetInfo;
        setTotalBalance();
    }

    private void setTotalBalance() {
        if(assetInfo != null) {
            availableBalance = availableBalance == null ? "0" : availableBalance;
            BigInteger availableIsaac = new BigInteger(BalanceUtils.EthToWei(availableBalance));
            BigInteger pendingBalance = new BigInteger(BalanceUtils.EthToWei(assetInfo.pendingTokens.total));
            mTotalBalance = BalanceUtils.weiToNEW(new BigInteger(BalanceUtils.EthToWei(assetInfo.stakingTokens.total)).add(availableIsaac).add(pendingBalance));
        } else {
            mTotalBalance = BalanceUtils.getStandardNewForShow(availableBalance);
        }
        summaryTotalBalanceTextView.setText(String.format("%s %s", StringUtil.formatAmount(mTotalBalance), C.NEW_SYMBOL));
        mViewModel.setTotalBalanceCache(currentWallet, mTotalBalance);
    }

    private void onCurrentWalletBalance(Map<String, String> balanceMap) {
        if (null != balanceMap) {
            String balance = balanceMap.get(getString(R.string.big_new));
            if (null != balance) {
                availableBalance = balance;
                setAvailableBalanceTextView(String.format("%s %s", StringUtil.formatAmount(BalanceUtils.weiToNEW(new BigInteger(BalanceUtils.EthToWei(availableBalance)))), getString(R.string.big_new)));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (null != result) {
            String resultContents = result.getContents();
            if (null != resultContents) {
                // http://
                if(resultContents.startsWith("http://") || resultContents.startsWith("https://")) {
                    return;
                }
                if(resultContents.startsWith(C.NEW_SYMBOL) || resultContents.startsWith(C.NEWTON_SYMBOL)) {
                    ScanResultInfo info = ScanUtils.parseScanResult(resultContents);
                    if (info != null) {
                        mViewModel.openSendWithAddress(getContext(), info.address, info.amount);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.no_address_scaned), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.no_address_scaned), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick({
            R.id.scanLinearLayout,
            R.id.sendLinearLayout,
            R.id.receiveLinearLayout,
            R.id.transactionTextView
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scanLinearLayout:
                if (null != mViewModel) {
                    mViewModel.openScan(this);
                }
                break;
            case R.id.sendLinearLayout:
                if (null != mViewModel) {
                    mViewModel.openSend(mContext);
                }
                break;
            case R.id.receiveLinearLayout:
                if (null != mViewModel && null != currentWallet) {
                    mViewModel.openReceive(mContext, currentWallet);
                }
                break;
            case R.id.transactionTextView:
                if (null != mViewModel) {
                    mViewModel.openWallet(mContext);
                }
                break;
        }
    }

    private void setAvailableBalanceTextView(String balance) {
        setTotalBalance();
    }

    @Override
    public void onPasswordComplete(String password) {
        sigmessage = StringUtil.getNonce();
        mViewModel.signMessageAndGetPublicKey(currentWallet.address, password, sigmessage);
    }

    private void onNewNetProofSubmit(NewNetProofSubmit newNetProofSubmit) {
        EventMessage<NewNetProofSubmit> message = new EventMessage<>(EventMessage.NEWNET_CACHE_PROOF, newNetProofSubmit);
        EventBus.getDefault().postSticky(message);
    }

    private void onNewNetPay(NewNetPay newNetPay) {
        EventMessage<NewNetPay> message = new EventMessage<>(EventMessage.NEWNET_CACHE_PAY, newNetPay);
        EventBus.getDefault().postSticky(message);
    }

    private void onNewNetAuth(NewNetAuth newNetAuth) {
        EventMessage<NewNetAuth> message = new EventMessage<>(EventMessage.NEWNET_CACHE_AUTH, newNetAuth);
        EventBus.getDefault().postSticky(message);
    }
}

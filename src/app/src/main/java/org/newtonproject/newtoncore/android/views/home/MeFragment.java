package org.newtonproject.newtoncore.android.views.home;

import android.arch.lifecycle.ViewModelProviders;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.entity.response.ProfileInfo;
import org.newtonproject.newtoncore.android.data.manager.ProfileManager;
import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.utils.ViewUtils;
import org.newtonproject.newtoncore.android.viewmodels.MeFragmentViewModel;
import org.newtonproject.newtoncore.android.viewmodels.MeFragmentViewModelFactory;
import org.newtonproject.newtoncore.android.views.base.BaseFragment;
import org.newtonproject.newtoncore.android.views.widget.OnCompletePasswordListener;

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
public class MeFragment extends BaseFragment<MeFragmentViewModel> implements OnCompletePasswordListener {

    private static final String TAG = "MeFragment";
    @BindView(R.id.centerTitle)
    TextView centerTitle;
    @BindView(R.id.settingImageView)
    ImageView settingImageView;
    @BindView(R.id.backupDivide)
    View backupDivide;

    @BindView(R.id.walletLinearLayout)
    LinearLayout walletLinearLayout;

    @BindView(R.id.backupLinearLayout)
    LinearLayout backupLinearLayout;

    @BindView(R.id.friendLinearLayout)
    LinearLayout friendLinearLayout;

    @BindView(R.id.rootView)
    LinearLayout rootView;

    @Inject
    MeFragmentViewModelFactory meFragmentViewModelFactory;

    private static final int SIGN_PROFILE = 1;

    private ProfileInfo profileInfo;

    private String currentAddress;
    private String sigmessage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, meFragmentViewModelFactory).get(MeFragmentViewModel.class);
        mViewModel.progress().observe(this, this::onProgress);
        mViewModel.onCurrentAddress().observe(this, this::onCurrentAddress);
        mViewModel.isNeedBackup().observe(this, this::isNeedBackup);
    }

    private void isNeedBackup(Boolean aBoolean) {
        backupLinearLayout.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        backupDivide.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int getTitle() {
        return -1;
    }

    private void onProgress(Boolean aBoolean) {
        onProgress(aBoolean, null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getDefaultWallet();
        mViewModel.checkNeedBackUp();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void onCurrentAddress(String address) {
        currentAddress = address;
        mViewModel.getCacheBalance(address);
        profileInfo = ProfileManager.getInstance().profileInfo();
        Logger.e(TAG, "ON Local profile");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(profileInfo == null && mViewModel != null) {
                mViewModel.getDefaultWallet();
            }
        }
    }

    @OnClick({R.id.settingImageView,
            R.id.walletLinearLayout,
            R.id.friendLinearLayout,
            R.id.backupLinearLayout
            })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //todo: maybe i can use {router} object replace them.
            case R.id.walletLinearLayout:
                mViewModel.openWalletList(mContext);
                break;
            case R.id.friendLinearLayout:
                mViewModel.openFriend(mContext, new Wallet(currentAddress));
                break;
            case R.id.settingImageView:
                mViewModel.openSetting(mContext);
                break;
            case R.id.backupLinearLayout:
                mViewModel.openBackUp(mContext);
                break;
        }
    }

    @Override
    public void onPasswordComplete(String password) {
        sigmessage = StringUtil.getNonce();
        mViewModel.signMessageAndGetPublicKey(currentAddress, password, sigmessage);
    }

}

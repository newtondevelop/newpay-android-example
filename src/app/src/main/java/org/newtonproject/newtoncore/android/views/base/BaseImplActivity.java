package org.newtonproject.newtoncore.android.views.base;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.utils.ToastUtils;
import org.newtonproject.newtoncore.android.viewmodels.BaseViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/12/20--3:45 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public abstract class BaseImplActivity<T extends BaseViewModel> extends BaseActivity {

    protected Context mContext;
    protected T mViewModel;
    protected Unbinder unbinder;
    @BindView(R.id.centerTitle)
    TextView centerTitle;
    protected AlertDialog dialog;
    protected String TAG = "BaseImplActivity";
    private Uri avatarUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //AndroidInjection.inject(this);
        injectActivity();
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        // init toolbar
        toolbar();
        // init toolbar color
        initTitleColor();
        initIntent();
        // set toolbar text
        int activityTitle = getActivityTitle();
        if(activityTitle != -1 && activityTitle != 0) {
            centerTitle.setText(getActivityTitle());
        }
        // init view model
        initView();

        initViewModel();
    }

    protected void initIntent() {}

    protected void injectActivity() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null) {
            unbinder.unbind();
        }
        if(mViewModel != null) {
            mViewModel.onCleared();
        }
    }

    protected void toast(String message) {
        ToastUtils.showNormalToast(mContext, message);
    }

    protected void toast(@StringRes int messageId) {
        ToastUtils.showNormalToast(mContext, messageId);
    }

    protected void onCommonError(Throwable throwable) {
        toast(throwable.getMessage());
    }

    protected void initView() {}

    protected void initTitleColor() {}

    protected void setCenterTitle(@StringRes int titleId) {
        centerTitle.setText(titleId);
    }

    protected void setCenterTitle(String title) {
        centerTitle.setText(title);
    }

    protected abstract @StringRes int getActivityTitle();

    protected abstract  @LayoutRes int getLayoutId();

    protected abstract void initViewModel();

    protected void onProgress(boolean shouldShowProgress, String title) {
        hideDialog();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(title == null ? getString(R.string.title_dialog_sending) : title)
                    .setView(new ProgressBar(this))
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
    }

    protected void progress(boolean isShow) {
        onProgress(isShow, null);
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

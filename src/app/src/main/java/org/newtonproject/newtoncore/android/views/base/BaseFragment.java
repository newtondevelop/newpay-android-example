package org.newtonproject.newtoncore.android.views.base;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.viewmodels.BaseViewModel;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/12/20--12:06 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public abstract class BaseFragment<T extends BaseViewModel> extends Fragment {

    protected Context mContext;
    protected View mView;
    protected Unbinder unbinder;
    protected T mViewModel;
    protected TextView toolbar;
    private AlertDialog dialog;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(mContext).inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mView);
        toolbar = mView.findViewById(R.id.centerTitle);
        int title = getTitle();
        if(toolbar != null && title != -1) {
            toolbar.setText(title);
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initViewModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
        }
        if(mViewModel != null) {
            mViewModel.onCleared();
        }
    }

    protected abstract @LayoutRes int getLayoutId();

    protected abstract void initViewModel();

    protected abstract @StringRes int getTitle();

    protected void initViews() {}

    protected void onProgress(boolean shouldShowProgress, String title) {
        hideDialog();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(mContext)
                    .setTitle(title == null ? getString(R.string.title_dialog_sending) : title)
                    .setView(new ProgressBar(mContext))
                    .setCancelable(true)
                    .create();
            dialog.show();
        }
    }

    protected void onProgress(boolean shouldShowProgress) {
        hideDialog();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(mContext)
                    .setTitle(getString(R.string.title_dialog_sending))
                    .setView(new ProgressBar(mContext))
                    .setCancelable(true)
                    .create();
            dialog.show();
        }
    }

    protected void onCommonError(Throwable throwable) {
        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}

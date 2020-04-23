package org.newtonproject.newtoncore.android.views.widget.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class FooterViewHolder extends BinderViewHolder {
    public static final int VIEW_TPYE = 1005;
    private static final String TAG = "FooterViewHolder";

    public TextView endTextView;
    public LinearLayout loadingLinearLayout;
    public TextView loadMoreTextView;

    public FooterViewHolder(int resId, ViewGroup parent) {
        super(resId, parent);
        endTextView = (TextView) findViewById(R.id.endTextView);
        loadingLinearLayout = (LinearLayout) findViewById(R.id.loadingLinearLayout);
        loadMoreTextView = (TextView) findViewById(R.id.loadMoreTextView);
    }

    @Override
    public void bind(@Nullable Object data, @NonNull Bundle addition) {

    }

    public void showEndView() {
        loadingLinearLayout.setVisibility(View.GONE);
        loadMoreTextView.setVisibility(View.GONE);
        endTextView.setVisibility(View.VISIBLE);
    }

    public void showLoadMoreView() {
        loadingLinearLayout.setVisibility(View.GONE);
        endTextView.setVisibility(View.GONE);
        loadMoreTextView.setVisibility(View.VISIBLE);
        loadMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    showLoadingView();
                    listener.onClick();
                }
            }
        });
    }

    public void showLoadingView() {
        loadingLinearLayout.setVisibility(View.VISIBLE);
        endTextView.setVisibility(View.GONE);
        loadMoreTextView.setVisibility(View.GONE);
    }

    public void hideFootView() {
        loadingLinearLayout.setVisibility(View.GONE);
        endTextView.setVisibility(View.GONE);
        loadMoreTextView.setVisibility(View.GONE);
    }

    public void setOnLoadMoreClickListener(OnLoadMoreClickListener onLoadMoreClickListener) {
        listener = onLoadMoreClickListener;
    }

    private OnLoadMoreClickListener listener;

    public interface OnLoadMoreClickListener{
        void onClick();
    }

}

package org.newtonproject.newtoncore.android.views.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public abstract class RecyclerScrollerListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "ListScrollListener";
    private int previousTotal = 0;
    private boolean isLoading = false;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private int currentPage = 0;
    private int totalPage = 0;
    private LinearLayoutManager linearLayoutManager;

    public RecyclerScrollerListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = linearLayoutManager.getItemCount();
        firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        int lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        if(isLoading && totalItemCount > previousTotal) {
            isLoading = false;
            previousTotal = totalItemCount;
        }
        if(!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            if(currentPage >= totalPage) {
                if(currentPage > 0 && lastVisibleItem + 1 != totalItemCount) {
                    onLoadEnd();
                }
                return;
            }
            if(currentPage != 0) {
                canLoadMore(currentPage + 1);
            }
            isLoading = true;
        }
    }

    protected abstract void canLoadMore(int currentPage);

    protected abstract void onLoadEnd();

    protected void showNormalEnd() {}

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    public void setRequestResult(int currentPage, int totalPage) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        isLoading = false;
    }
}

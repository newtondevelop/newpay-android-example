package org.newtonproject.newtoncore.android.views.transaction;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.utils.RootUtil;
import org.newtonproject.newtoncore.android.viewmodels.TransactionsViewModel;
import org.newtonproject.newtoncore.android.viewmodels.TransactionsViewModelFactory;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.views.widget.RecyclerScrollerListener;
import org.newtonproject.newtoncore.android.views.widget.adapter.TransactionsAdapter;
import org.newtonproject.newtoncore.android.widget.DateChoosePopuwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionsActivity extends BaseImplActivity<TransactionsViewModel> implements View.OnClickListener {

    @Inject
    TransactionsViewModelFactory transactionsViewModelFactory;

    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.emptyView)
    View emptyView;

    private Wallet mWallet;
    private TransactionsAdapter adapter;
    private static final String TAG = "TransactionsActivity";
    private RecyclerScrollerListener listScrollListener;
    private ArrayList<String> dateList;
    private LinearLayoutManager transactionListLayoutManager;
    private HashMap<String, Integer> datePositionMap = new HashMap<>();
    private int loadMorePage = 1;

    @Override
    protected int getActivityTitle() {
        return R.string.transaction_record;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transactions_with_account;
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, transactionsViewModelFactory)
                .get(TransactionsViewModel.class);
        mViewModel.progress().observe(this, this::progress);
        mViewModel.onCommonError().observe(this, this::onCommonError);
        mViewModel.defaultWallet().observe(this, this::onDefaultWallet);
        // get transaction
        mViewModel.transactions().observe(this, this::onTransactions);
        mViewModel.onTransactionError().observe(this, this::onTransactionError);
        checkRoot();
        mViewModel.prepare();
    }

    private void onTransactionError(Throwable throwable) {
        toast(throwable.getMessage());
        adapter.showEndView();
    }

    @Override
    protected void initView() {
        transactionListLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(transactionListLayoutManager);
        listScrollListener = new RecyclerScrollerListener(transactionListLayoutManager) {
            @Override
            protected void canLoadMore(int currentPage) {
                if(loadMorePage != currentPage) {
                    loadMorePage = currentPage;
                    adapter.showLoadingMore();
                    mViewModel.fetchTransactions(mWallet, loadMorePage, true);
                }
            }

            @Override
            protected void onLoadEnd() {
                adapter.showEndView();
            }
        };
        recyclerView.addOnScrollListener(listScrollListener);
        refreshLayout.setOnRefreshListener(() -> {
            loadMorePage = 1;
            fetchTransactionsAndBalance(true);
        });
    }

    private void onTransactions(TransactionResponse transactionResponse) {
        // get transaction then set total page and current page
        if(loadMorePage == 1) {
            adapter.clear();
        }
        Transaction[] transaction = transactionResponse.docs;
        int currentPage = transactionResponse.page;
        int totalPage = transactionResponse.pages;
        listScrollListener.setRequestResult(currentPage, totalPage);
        if(transaction == null || transaction.length == 0){
            emptyView.setVisibility(View.VISIBLE);
        } else{
            emptyView.setVisibility(View.GONE);
            datePositionMap = adapter.addTransactions(transaction);
            Set<String> strings = datePositionMap.keySet();
            dateList = new ArrayList<>();
            dateList.addAll(strings);
            if(loadMorePage == 1) {
                recyclerView.scrollToPosition(0);
            }
        }
    }

    @Override
    protected void progress(boolean isShow) {
        refreshLayout.setRefreshing(isShow);
    }

    private void fetchTransactionsAndBalance(boolean forceUpdate) {
        mViewModel.fetchTransactions(mWallet, loadMorePage, forceUpdate);
        mViewModel.getBalance(mWallet);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.onCleared();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_filter:
                if(dateList != null && dateList.size() > 0) {
                    showDateChoosePop();
                }else{
                    Toast.makeText(this, R.string.no_transactions_yet, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDateChoosePop() {
        DateChoosePopuwindow popuwindow = new DateChoosePopuwindow(this, dateList);
        popuwindow.setSelectedListener(date -> {
            Integer integer = datePositionMap.get(date);
            transactionListLayoutManager.scrollToPositionWithOffset(integer, 0);
        });
        popuwindow.showAtLocation(root, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }

    private void onTransactionClick(View view, Transaction transaction) {
        mViewModel.showDetails(view.getContext(), transaction);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again: {
                mViewModel.fetchTransactions(mWallet, loadMorePage, true);
            }
            break;
        }
    }

    private void onDefaultWallet(Wallet wallet) {
        mWallet = wallet;
        // fill recycle view
        adapter = new TransactionsAdapter(this::onTransactionClick, mWallet);
        adapter.setOnLoadMoreClickListener(() -> mViewModel.fetchTransactions(mWallet, loadMorePage, true));
        adapter.clear();
        recyclerView.setAdapter(adapter);
        mViewModel.fetchTransactions(mWallet, loadMorePage,false);
    }

    private void checkRoot() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (RootUtil.isDeviceRooted() && pref.getBoolean("should_show_root_warning", true)) {
            pref.edit().putBoolean("should_show_root_warning", false).apply();
            new AlertDialog.Builder(this)
                    .setTitle(R.string.root_title)
                    .setMessage(R.string.root_body)
                    .setNegativeButton(R.string.ok, (dialog, which) -> {
                    })
                    .show();
        }
    }
}

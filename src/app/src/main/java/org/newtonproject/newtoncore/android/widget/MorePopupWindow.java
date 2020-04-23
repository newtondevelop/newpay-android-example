package org.newtonproject.newtoncore.android.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.widget.adapter.MoreItemAdapter;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-07-04--14:52
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class MorePopupWindow extends PopupWindow {

    private final Context mContext;
    private final MoreItemAdapter.OnItemClickListener onItemClickListener;
    private final ArrayList<String> items;
    private View view;

    public MorePopupWindow(Context context, ArrayList<String> list, MoreItemAdapter.OnItemClickListener listener) {
        mContext = context;
        onItemClickListener = listener;
        items = list;
        LayoutInflater inflater = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (ViewGroup) inflater.inflate(R.layout.popupwindow_more, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        MoreItemAdapter moreItemAdapter = new MoreItemAdapter(items);
        recyclerView.addItemDecoration(new SampleItemDecoration(mContext));
        moreItemAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(moreItemAdapter);
    }

}

package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.views.widget.holder.BinderViewHolder;
import org.newtonproject.newtoncore.android.views.widget.holder.ContactHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ContactsAdapter extends RecyclerView.Adapter<BinderViewHolder>{
    private ArrayList<ContactsInfo> contactsInfos;
    private ArrayList<Integer> viewTypeList;
    private HashMap<Integer, Object> positionObjectMap;
    private HashMap<String, Integer> firstPinyinPositionMap;
    private int totalCount = 0;

    private final OnFriendItemClickListener onFriendItemClickListener;
    private final OnFriendItemLongClickListener onFriendItemLongClickListener;

    public ContactsAdapter(OnFriendItemClickListener onFriendItemClickListener, OnFriendItemLongClickListener onFriendItemLongClickListener) {
        this.onFriendItemClickListener = onFriendItemClickListener;
        this.onFriendItemLongClickListener = onFriendItemLongClickListener;
    }

    @Override
    public BinderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BinderViewHolder binderViewHolder = null;
        switch (viewType) {
            case ContactHolder.VIEW_TYPE: {
                ContactHolder holder = new ContactHolder(R.layout.item_friend, parent);
                holder.setOnFriendItemClickListener(onFriendItemClickListener);
                holder.setOnFriendItemLongClickListener(onFriendItemLongClickListener);
                binderViewHolder = holder;
            }break;
            case ContactHolder.VIEW_INDICATOR_TYPE:
                ContactNamePinYinHolder holder = new ContactNamePinYinHolder(R.layout.item_friend_divider, parent);
                binderViewHolder = holder;
                break;
        }
        return binderViewHolder;
    }

    @Override
    public void onBindViewHolder(BinderViewHolder holder, int position) {
        Object o = positionObjectMap.get(position);
        if(o instanceof ContactsInfo) {
            ContactsInfo info = (ContactsInfo) positionObjectMap.get(position);
            holder.bind(info,null);
        }else if(o instanceof String) {
            String s  = (String) positionObjectMap.get(position);
            holder.bind(s);
        }
    }

    @Override
    public int getItemCount() {
        return totalCount;
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeList.get(position);
    }

    public int getPositionByHeader(String text) {
        if(firstPinyinPositionMap == null || firstPinyinPositionMap.size() == 0) {
            return -1;
        }
        boolean b = firstPinyinPositionMap.containsKey(text);
        if(b) {
            return firstPinyinPositionMap.get(text);
        }else{
            return -1;
        }
    }

    public void setContacts(ArrayList<ContactsInfo> contactsInfos) {
        this.contactsInfos = contactsInfos == null ? new ArrayList<>() : contactsInfos;
        checkTotalCount();
        notifyDataSetChanged();
    }

    public String[] getFirstPinyin() {
        if(firstPinyinPositionMap != null && firstPinyinPositionMap.size() > 0) {
            Set<String> strings = firstPinyinPositionMap.keySet();
            return strings.toArray(new String[]{});
        }
        return null;
    }

    private void checkTotalCount() {
        if(contactsInfos != null && contactsInfos.size() > 0) {
            viewTypeList = new ArrayList<>();
            positionObjectMap = new HashMap<>();
            firstPinyinPositionMap = new HashMap<>();
            int index = 0;
            for(int i = 0; i < contactsInfos.size(); i ++) {
                ContactsInfo info = contactsInfos.get(i);
                if(i == 0) {
                    viewTypeList.add(ContactHolder.VIEW_INDICATOR_TYPE);
                    positionObjectMap.put(index, info.firstPinYin);
                    firstPinyinPositionMap.put(info.firstPinYin, index);
                    index = index + 1;

                    viewTypeList.add(ContactHolder.VIEW_TYPE);
                    positionObjectMap.put(index, info);
                    index = index + 1;
                }else{
                    ContactsInfo preinfo = contactsInfos.get(i - 1);
                    if(preinfo.getFirstPinYin().equals(info.firstPinYin)) {
                        viewTypeList.add(ContactHolder.VIEW_TYPE);
                        positionObjectMap.put(index, info);
                        index = index + 1;
                    }else{
                        viewTypeList.add(ContactHolder.VIEW_INDICATOR_TYPE);
                        positionObjectMap.put(index, info.firstPinYin);
                        firstPinyinPositionMap.put(info.firstPinYin, index);
                        index = index + 1;

                        viewTypeList.add(ContactHolder.VIEW_TYPE);
                        positionObjectMap.put(index, info);
                        index = index + 1;
                    }
                }
            }
            totalCount = index;
        }else{
            viewTypeList = null;
            positionObjectMap = null;
            firstPinyinPositionMap = null;
            totalCount = 0;
        }
    }

    public interface OnFriendItemClickListener {
        void onResult(String selectedAddress);
    }

    public interface OnFriendItemLongClickListener {
        void onLongClickResult(String trim, ViewGroup parent);
    }



    class ContactNamePinYinHolder extends BinderViewHolder{
        private TextView textView;
        public ContactNamePinYinHolder(int resId, ViewGroup parent) {
            super(resId, parent);
            textView = (TextView) findViewById(R.id.indicatorTextView);
        }

        @Override
        public void bind(@Nullable Object data, @NonNull Bundle addition) {
            textView.setText(data.toString());
        }
    }
}

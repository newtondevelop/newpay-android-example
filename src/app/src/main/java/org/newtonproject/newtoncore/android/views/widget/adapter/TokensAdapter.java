package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Token;
import org.newtonproject.newtoncore.android.views.widget.OnTokenClickListener;
import org.newtonproject.newtoncore.android.views.widget.holder.TokenHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TokensAdapter extends RecyclerView.Adapter<TokenHolder> {

    private final OnTokenClickListener onTokenClickListener;
    private final List<Token> items = new ArrayList<>();

    public TokensAdapter(OnTokenClickListener onTokenClickListener) {
        this.onTokenClickListener = onTokenClickListener;
    }

    @Override
    public TokenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TokenHolder tokenHolder = new TokenHolder(R.layout.item_token, parent);
        tokenHolder.setOnTokenClickListener(onTokenClickListener);
        return tokenHolder;
    }

    @Override
    public void onBindViewHolder(TokenHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setTokens(Token[] tokens) {
        items.clear();
        items.addAll(Arrays.asList(tokens));
        notifyDataSetChanged();
    }
}

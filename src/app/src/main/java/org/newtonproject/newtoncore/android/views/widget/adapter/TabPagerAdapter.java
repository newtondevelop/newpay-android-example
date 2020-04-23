package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private final List<Pair<String, Fragment>> pages;

    public TabPagerAdapter(FragmentManager fm, List<Pair<String, Fragment>> pages) {
        super(fm);

        this.pages = pages;
    }

    // Return fragment with respect to position.
    @Override
    public Fragment getItem(int position) {
        return pages.get(position).second;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    // This method returns the title of the tab according to its position.
    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).first;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}

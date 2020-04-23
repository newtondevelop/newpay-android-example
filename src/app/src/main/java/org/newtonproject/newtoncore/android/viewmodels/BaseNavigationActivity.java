package org.newtonproject.newtoncore.android.viewmodels;

import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import org.newtonproject.newtoncore.android.views.base.BaseActivity;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class BaseNavigationActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;

    protected void initBottomNavigation() {
        //navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    protected void setBottomMenu(@MenuRes int menuRes) {
        navigation.getMenu().clear();
        navigation.inflateMenu(menuRes);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    protected void setCurrentItem(int menuId) {
        navigation.setSelectedItemId(menuId);
        navigation.getSelectedItemId();
    }


}

package org.newtonproject.newtoncore.android.views.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.utils.LanguageUtils;
import org.newtonproject.newtoncore.android.utils.UIToolkit;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public abstract class BaseActivity extends AppCompatActivity {
	private View statusBarView;
	protected Toolbar toolbar;
	protected @DrawableRes int statuBarDrawable = -1;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLanguageLocale();
		setStatusBarColor();
		initStatusBarColor();

	}

	protected void setStatusBarColor() {}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void initStatusBarColor() {
		Looper.myQueue().addIdleHandler(() -> {
			if (isStatusBar()) {
				initStatusBar();
				getWindow().getDecorView().addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> initStatusBar());
			}
			return false;
		});
	}

	protected void setLanguageLocale() {
		if (!LanguageUtils.isSameLanguage(this)) {
			LanguageUtils.setLocale(this);
		}
	}

	private void initStatusBar() {
		if (statusBarView == null) {
			int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
			statusBarView = getWindow().findViewById(identifier);
		}
		if (statusBarView != null) {
			statusBarView.setBackgroundResource(statuBarDrawable == -1 ? R.color.bgColor : statuBarDrawable);
		}
	}

	protected boolean isStatusBar() {
		return true;
	}

	protected Toolbar toolbar() {
		toolbar = findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setTitle(getTitle());
		}
		enableDisplayHomeAsUp();
		setTitle(null);
		return toolbar;
	}

	protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setSubtitle(String subtitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

	protected void enableDisplayHomeAsUp() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	protected void dissableDisplayHomeAsUp() {/**/
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

	protected void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
	    if (actionBar != null) {
	        actionBar.hide();
        }
    }

    protected void showToolbar() {
        ActionBar actionBar = getSupportActionBar();
	    if (actionBar != null) {
	        actionBar.show();
        }
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!UIToolkit.isRunningForeground()) {
			// enter background
			C.IS_CHECK_BACKUP = true;
		}
	}
}

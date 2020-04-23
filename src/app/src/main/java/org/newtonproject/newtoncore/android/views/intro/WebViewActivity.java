package org.newtonproject.newtoncore.android.views.intro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.App;
import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.utils.LanguageUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WebViewActivity extends BaseActivity {

    private static final String TAG = "WebViewActivity";
    private WebView webView;
    private String url;
    private String title;
    private ProgressBar progressbar;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_activity);
        context = this;
        url = getIntent().getStringExtra(C.EXTRA_URL);
        title = getIntent().getStringExtra(C.EXTRA_TITLE);
        toolbar();
        TextView textView = findViewById(R.id.centerTitle);
        textView.setText(title);
        webView = findViewById(R.id.webview);
        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        initView();
    }

    private void initView() {
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress < 100) {
                    progressbar.setProgress(newProgress);
                }else {
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            Logger.d("current sdk is:" + Build.VERSION.SDK_INT);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                    builder.setPositiveButton(R.string.confirm, (dialog, which) -> handler.proceed());
                    builder.setNegativeButton(R.string.cancel, (dialog, which) -> handler.cancel());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    url = request.getUrl().toString();
                    Logger.d("url is:" + url);
                    if (TextUtils.isEmpty(url)) return false;
                    try {
                        if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                            Uri parse = Uri.parse(url);
                            Logger.d(parse.toString());
                            Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                            if (isInstall(intent)) {
                                startActivity(intent);
                                finish();
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        return false;
                    }
                    return false;
                }
            });
        } else {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                    builder.setPositiveButton(R.string.confirm, (dialog, which) -> handler.proceed());
                    builder.setNegativeButton(R.string.cancel, (dialog, which) -> handler.cancel());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (TextUtils.isEmpty(url)) return false;
                    Logger.d("override url loading:" + url);
                    try {
                        if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                            Uri parse = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            if (isInstall(intent)) {
                                startActivity(intent);
                                finish();
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                        return false;
                    }
                    return false;
                }
            });
        }
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if(!TextUtils.isEmpty(url)) {
            syncCookie(this, url);
            webView.loadUrl(url);
        }
    }

    private void syncCookie(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.setCookie(url, String.format("language=%s", LanguageUtils.getLanguage(new SharedPreferenceRepository(this).getSharePreferenceLanguageType())));
        CookieSyncManager.getInstance().sync();// To get instant sync instead of waiting for the timer to trigger, the host can call this.
    }

    private boolean isInstall(Intent intent) {
        return App.getAppContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}

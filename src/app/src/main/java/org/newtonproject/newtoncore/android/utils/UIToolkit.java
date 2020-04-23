package org.newtonproject.newtoncore.android.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;

import org.newtonproject.newtoncore.android.App;
import org.newtonproject.newtoncore.android.BuildConfig;
import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.account.WelcomeActivity;
import org.newtonproject.newtoncore.android.views.home.HomeActivity;
import org.newtonproject.newtoncore.android.views.widget.OnupdateCancelListener;
import org.newtonproject.newtoncore.android.widget.UpdateDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UIToolkit {
    private static final String TAG = "UIToolkit";
    private static boolean isForceUpgrade = false;
    static UpdateDialog updateDialog;
    public static boolean isShowDialog = false;
    private View.OnClickListener leftclickListener;
    private OnupdateCancelListener onupdateCancelListener;

    public static void openUrl(String downloadUrl, Activity context) {
        Logger.e(TAG, Thread.currentThread().getName());
        if (isShowDialog) {
            DownloadTask downloadTask = new DownloadTask(context, downloadUrl);
            downloadTask.start();
            updateDialog.showProgressView();
            downloadTask.setDownloadListener(new DownloadTask.DownloadListener() {
                @Override
                public void onProgressUpdate(int progress) {
                    updateDialog.updateProgress(progress);
                }

                @Override
                public void onFinish() {
                    File apkfile = new File(downloadTask.getSavedFilePath());
                    if (!apkfile.exists()) {
                        Logger.e(TAG, "No apk installed");
                        ToastUtils.showNormalToast(context, StringUtil.getString(R.string.install_problem));
                    } else {
                        if (Build.VERSION.SDK_INT >= 26) {
                            boolean b = context.getPackageManager().canRequestPackageInstalls();
                            if (b) {
                                installApk(context, apkfile);
                            } else {
                                Logger.e(TAG, "onFinish: no permission" );
                                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, C.PERMISSION_CODE_INSTALL_PACKAGES);
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                context.startActivityForResult(intent, C.REQUEST_CODE_GET_UNKNOWN_APP_SOURCES);
                                if(context instanceof HomeActivity) {
                                    ((HomeActivity) context).setInstallFile(apkfile);
                                }
                                if(context instanceof WelcomeActivity) {
                                    ((WelcomeActivity) context).setInstallFile(apkfile);
                                }
                            }
                        } else {
                            installApk(context, apkfile);
                        }
                    }
                    updateDialog.dismiss();
                    isShowDialog = false;
                }

                @Override
                public void onIOException() {
                    ToastUtils.showToastCenterShort(context, StringUtil.getString(R.string.download_problem));
                    if (isForceUpgrade) {
                        updateDialog.showRetryViewIsForce();
                    } else {
                        updateDialog.showRetryView();
                    }
                }

                @Override
                public void onFinally() {
                    Logger.e(TAG, "onFinally: complete");
                }
            });
        }
    }

    public static void installApk(Activity context, File apkfile) {
        try {
            String command = "chmod 777 " + apkfile.getAbsolutePath();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.AUTHORITY, apkfile);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                i.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            }
            context.startActivity(i);
            context.finish();
        } catch (IOException e) {
            Logger.e(TAG, "Install Error");
            ToastUtils.showNormalToast(context, StringUtil.getString(R.string.install_problem));
        }
    }

    public static boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(2);
        if (list.get(0).topActivity.getPackageName().equals(App.getAppContext().getPackageName())) {
            return true;
        }
        return false;
    }
}

package org.newtonproject.newtoncore.android.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.newtonproject.newtoncore.android.C.DOWNLOAD_FILE_FOLDER;
import static org.newtonproject.newtoncore.android.C.DOWNLOAD_FILE_NM_PREFIX;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class DownloadTask extends Thread{
    private static final String TAG = "DownloadTask";
    final long FREESPACE = 100 * 1024 * 1024;

    public interface DownloadListener {
        void onProgressUpdate(int progress);
        void onFinish();
        void onIOException();
        void onFinally();
    }

    DownloadListener downloadListener;

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    String saveFileFullPath;
    String download_url;
    int count = 0;
    int length = 0;

    WeakReference<Activity> weakContext;
    volatile int progress;
    String savePath = "";
    String saveFileName;
    String specificDir = null;
    public String getSavedFilePath() {
        return saveFileFullPath;
    }

    public DownloadTask(Activity context, String download_url) {
        this.download_url = download_url;
        weakContext = new WeakReference<Activity>(context);
    }

    public DownloadTask(Activity context, String download_url, String saveDir) {
        this.download_url = download_url;
        weakContext = new WeakReference<Activity>(context);
        specificDir = saveDir;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            if (weakContext.get() != null && weakContext.get().getFilesDir().getFreeSpace() >= FREESPACE) {
                if(null != specificDir) {
                    savePath = specificDir;
                } else {
                    savePath = weakContext.get().getFilesDir().getAbsolutePath();
                }
            }else {
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    int requestWriteState = ContextCompat.checkSelfPermission(weakContext.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (requestWriteState != PackageManager.PERMISSION_GRANTED) {
                        weakContext.get().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToastCenterShort(weakContext.get(), StringUtil.getString(R.string.withoutWriteStoragePermission));
                            }
                        });
                        ActivityCompat.requestPermissions(weakContext.get(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, C.PERMISSION_CODE_WRITE_EXTERNAL_STORAGE);
                        return;
                    }else {
                        savePath = Environment.getExternalStorageDirectory() + File.separator + DOWNLOAD_FILE_FOLDER;
                    }
                }else{
                    return;
                }
            }
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdir();
            }
            if(download_url.endsWith(".zip")) {
                saveFileName = "www.zip";
            } else {
                FileUtils.deleteFileByPrefixAndPostfix(savePath, DOWNLOAD_FILE_NM_PREFIX, ".apk");
                saveFileName = DOWNLOAD_FILE_NM_PREFIX + System.currentTimeMillis() + ".apk";
            }

            saveFileFullPath = savePath + "/" + saveFileName;
            File ApkFile = new File(saveFileFullPath);
            if (!ApkFile.exists()) {
                ApkFile.createNewFile();
            }

            fos = new FileOutputStream(ApkFile);
            URL url = new URL(download_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            length = conn.getContentLength();
            is = conn.getInputStream();
            byte buf[] = new byte[1024];
            count = 0;
            do {
                int numread = is.read(buf);
                count += numread;
                progress = (int) (((float) count / length) * 100);
                if (weakContext.get() == null) {
                    return;
                }
                weakContext.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (downloadListener != null) {
                            downloadListener.onProgressUpdate(progress);
                        }
                    }
                });
                if (numread <= 0) {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                    }
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                    try {
                        fos.close();
                    } catch (Exception e) {

                    }
                    progress = 100;
                    if (weakContext.get() == null) {
                        return;
                    }
                    weakContext.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (downloadListener != null) {
                                downloadListener.onFinish();
                            }
                        }
                    });
                    break;
                }
                fos.write(buf, 0, numread);
            } while (!Thread.currentThread().isInterrupted());
        } catch (IOException e) {
            if (Thread.currentThread().isInterrupted()) {
                e.printStackTrace();
                return;
            }
            if (weakContext.get() == null) {
                e.printStackTrace();
                return;
            }
            weakContext.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (downloadListener != null) {
                        e.printStackTrace();
                        downloadListener.onIOException();
                    }
                }
            });
        } finally {
            try {
                conn.disconnect();
            } catch (Exception e) {
            }
            try {
                is.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {

            }
            if (weakContext.get() == null) {
                return;
            }
            weakContext.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (downloadListener != null) {
                        downloadListener.onFinally();
                    }
                }
            });
        }
    }
}

package org.newtonproject.newtoncore.android.utils;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;
import android.util.Log;

import org.newtonproject.newtoncore.android.App;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/11/7--7:44 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class MediaUtils {
    public static String TAG = "MediaUtils";
    public static void startPlay(@RawRes int resId) {
        Disposable subscribe = Observable.just(resId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    MediaPlayer mediaPlayer1 = MediaPlayer.create(App.getAppContext(), file);
                    mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer1.start();
                }, error-> Log.e(TAG, "startPlay: " + error.getMessage() ));
    }
}

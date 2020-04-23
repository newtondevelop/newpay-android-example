package org.newtonproject.newtoncore.android.data.manager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.greenrobot.eventbus.EventBus;
import org.newtonproject.newtoncore.android.data.entity.response.ProfileInfo;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/4/17--6:01 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ProfileManager {

    private static ProfileManager ourInstance;
    private static ProfileInfo mProfileInfo;
    private ProfileManager() {}

    // init observer profile
    private MutableLiveData<ProfileInfo> observableProfileInfo = new MutableLiveData<>();
    public LiveData<ProfileInfo> observableProfileInfo() {
        return observableProfileInfo;
    }

    // init instance
    public static ProfileManager getInstance() {
        if (ourInstance == null) {
            synchronized (EventBus.class) {
                if (ourInstance == null) {
                    ourInstance = new ProfileManager();
                }
            }
        }
        return ourInstance;
    }

    // check profile info
    public boolean hasProfile() {
        return mProfileInfo == null;
    }

    public ProfileInfo profileInfo() {
        return mProfileInfo;
    }

    public void updateProfile(ProfileInfo profileInfo) {
        if(profileInfo == null) {
            throw new IllegalArgumentException("Profile info can not be null");
        }
        if(mProfileInfo == null) {
            mProfileInfo = profileInfo;
        } else {
            mProfileInfo.updateProfile(profileInfo);
        }
        observableProfileInfo.postValue(mProfileInfo);
    }

    public void clear() {
        observableProfileInfo.postValue(null);
        mProfileInfo = null;
    }
}

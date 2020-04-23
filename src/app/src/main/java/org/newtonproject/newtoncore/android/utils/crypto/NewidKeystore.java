package org.newtonproject.newtoncore.android.utils.crypto;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.newtonproject.newtoncore.android.data.entity.response.ProfileInfo;
import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewidKeystore {
    private String TAG = NewidKeystore.class.getSimpleName();

    private String keyStorePath;

    public NewidKeystore(String keyStorePath) {
        if(!keyStorePath.endsWith("/")){
            keyStorePath = keyStorePath + "/";
        }
        this.keyStorePath = keyStorePath;
        File file = new File(keyStorePath);
        if(!file.exists() || !file.isDirectory()){
            file.mkdirs();
        }
    }

    /**
     * create profile info
     * @param info
     * @return
     * @throws IOException
     */
    public synchronized ProfileInfo createProfileInfo(ProfileInfo info) throws IOException{
        info.address = NewAddressUtils.newAddress2HexAddress(info.address);
        boolean hasExit = checkIsExistKeystore(info.address);
        //todo: delete the profile and create a new profile keystore.
        if(hasExit) {
            deleteAccount(info.address);
        }
        String fileName = getProfileFileName(info);
        File file = new File(keyStorePath, fileName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, info);
        return info;
    }

    public ProfileInfo fetchProfileInfo(String address) throws IOException{
        String keyStorePath = getKeyStorePath(address);
        Logger.d(TAG, "path:" + keyStorePath);
        if(keyStorePath != null) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(keyStorePath), ProfileInfo.class);
        }
        return null;
    }

    public synchronized String getAccessKey(String address) throws IOException{
        String keyStorePath = getKeyStorePath(address);
        if(keyStorePath != null) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(keyStorePath), ProfileInfo.class).accessKey;
        }
        return null;
    }

    public boolean deleteAccount(String address) throws IOException {
        String newidFile = getKeyStorePath(address);
        if(newidFile != null) {
            File keyStore = new File(newidFile);
            return keyStore.delete();
        }
        return false;
    }

    public synchronized boolean updateAvatar(String address, String avatarPath) throws IOException{
        String keyStorePath = getKeyStorePath(address);
        if(keyStorePath != null && !TextUtils.isEmpty(avatarPath)) {
            ObjectMapper mapper = new ObjectMapper();
            File keyStore = new File(keyStorePath);
            ProfileInfo info = mapper.readValue(keyStore, ProfileInfo.class);
            info.avatarPath = avatarPath;
            mapper.writeValue(keyStore, info);
            return true;
        }
        return false;
    }

    public synchronized boolean updateCellphone(String address, String countryCode, String cellphone) throws IOException{
        String keyStorePath = getKeyStorePath(address);
        if(keyStorePath != null && countryCode != null && cellphone != null) {
            ObjectMapper mapper = new ObjectMapper();
            File keyStore = new File(keyStorePath);
            ProfileInfo info = mapper.readValue(keyStore, ProfileInfo.class);
            info.countryCode = countryCode;
            info.cellphone = cellphone;
            mapper.writeValue(keyStore, info);
            return true;
        }
        return false;
    }

    public synchronized boolean updateProfileName(String address, String name) throws IOException{
        String keyStorePath = getKeyStorePath(address);
        if(keyStorePath != null && name != null) {
            ObjectMapper mapper = new ObjectMapper();
            File keyStore = new File(keyStorePath);
            ProfileInfo info = mapper.readValue(keyStore, ProfileInfo.class);
            info.name = name;
            mapper.writeValue(keyStore, info);
            return true;
        }
        return false;
    }


    public synchronized ProfileInfo updateLocalProfile(String address, ProfileInfo newProfile) throws IOException {
        String keyStorePath = getKeyStorePath(address);
        if(keyStorePath != null && newProfile != null) {
            ObjectMapper mapper = new ObjectMapper();
            File keyStore = new File(keyStorePath);
            ProfileInfo info = mapper.readValue(keyStore, ProfileInfo.class);
            if(info.equals(newProfile)) {
                return info;
            }
            info.updateProfile(newProfile);
            mapper.writeValue(keyStore, info);
            return info;
        }
        return null;
    }

    private String getKeyStorePath(String address) {
        File file = new File(keyStorePath);
        if(TextUtils.isEmpty(address)) return null;
        address = NewAddressUtils.newAddress2HexAddress(address);
        if(file.exists() && file.isDirectory()) {
            // loop keystore directory.
            String[] list = file.list();
            if(list != null) {
                for(String fileName : list) {
                    if(fileName.contains(address)){
                        return keyStorePath + fileName;
                    }
                }
            }
        }
        return null;
    }

    private String getProfileFileName(ProfileInfo profileInfo) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'");
        return dateFormat.format(new Date()) + profileInfo.address + "--profile.json";
    }

    /**
     * Check has wallet by address.
     * @param address
     * @return
     */
    private boolean checkIsExistKeystore(String address) {
        String keyStorePath = getKeyStorePath(address);
        return keyStorePath != null;
    }

}

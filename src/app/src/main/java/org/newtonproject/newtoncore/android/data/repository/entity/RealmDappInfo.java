package org.newtonproject.newtoncore.android.data.repository.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class RealmDappInfo extends RealmObject {
    @PrimaryKey
    private String bundleId;

    private String dappId;
    private String dappName;
    private String dappUrl;
    private String dappImage;
    private String dappVersion;
    private String dappDesc;
    private String dappDownloadUrl;
    private String dappOwner;
    private int dappStatus;
    private int type;
    private String md5;
    private int openType;

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }


    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getDappId() {
        return dappId;
    }

    public void setDappId(String dappId) {
        this.dappId = dappId;
    }

    public String getDappName() {
        return dappName;
    }

    public void setDappName(String dappName) {
        this.dappName = dappName;
    }

    public String getDappUrl() {
        return dappUrl;
    }

    public void setDappUrl(String dappUrl) {
        this.dappUrl = dappUrl;
    }

    public String getDappImage() {
        return dappImage;
    }

    public void setDappImage(String dappImage) {
        this.dappImage = dappImage;
    }

    public String getDappVersion() {
        return dappVersion;
    }

    public void setDappVersion(String dappVersion) {
        this.dappVersion = dappVersion;
    }

    public String getDappDesc() {
        return dappDesc;
    }

    public void setDappDesc(String dappDesc) {
        this.dappDesc = dappDesc;
    }

    public String getDappDownloadUrl() {
        return dappDownloadUrl;
    }

    public void setDappDownloadUrl(String dappDownloadUrl) {
        this.dappDownloadUrl = dappDownloadUrl;
    }

    public String getDappOwner() {
        return dappOwner;
    }

    public void setDappOwner(String dappOwner) {
        this.dappOwner = dappOwner;
    }

    public int getDappStatus() {
        return dappStatus;
    }

    public void setDappStatus(int dappStatus) {
        this.dappStatus = dappStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

}

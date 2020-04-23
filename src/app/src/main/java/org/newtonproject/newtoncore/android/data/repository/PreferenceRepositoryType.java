package org.newtonproject.newtoncore.android.data.repository;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface PreferenceRepositoryType {
	String getCurrentWalletAddress();
	void setCurrentWalletAddress(String address);

	String getDefaultNetwork();
	void setDefaultNetwork(String netName);

	String getWalletName(String address);
	void setWalletName(String address, String name);

	int getSharePreferenceLanguageType();
	void setSharePreferenceLange(int languageType);

	String getCacheBalance(String addres);
	void setCacheBalance(String address, String balance);

	String getIMEI();
	void setIMEI(String imei);

	boolean isNeedPing();
	void setIsNeedPing(boolean isNeedPing);

	boolean isPin();
	void setIsPin(boolean isPin);

	void setIsCheckPin(boolean isCheckPin);

	String getPinPassword();
	void setPinPassword(String password);

	boolean isSupportFinger();
	void setIsSupportFinger(boolean isSupportFinger);

	String getTotalBalance(String address);
	void setTotalBalance(String address, String balance);

	void setInviteCode(String inviteCode);

	void clearPreference();

    boolean isNeedBackup();
    void setNeedBackUp(boolean isNeedBackup);

}

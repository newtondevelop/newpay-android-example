package org.newtonproject.newtoncore.android.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.newtonproject.newtoncore.android.C;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SharedPreferenceRepository implements PreferenceRepositoryType {

	private static final String CURRENT_ACCOUNT_ADDRESS_KEY = "current_account_address";
	private static final String DEFAULT_NETWORK_NAME_KEY = "default_network_name";
	private static final String DEFAULT_WALLET_NAME = "My Wallet";
	private static final String LANGUAGE_KEY = "local_language";
	private static final String CACHE_BALANCE_PREFIX = "cache_balance";
	private static final String CACHE_TOTAL_BALANCE_PREFIX = "cache_total_balance";

	private static final String PING_FLAG = "isPing";
	private static final String IS_PIN = "is_pin";
	private static final String IS_CHECK_PIN = "is_check_pin";
	private static final String PIN_PASSWORD = "pin_password";
	private static final String IS_SUPPORT_FINGER = "is_support_finger";
	private static final String INVITE_DODE = "invite_code";
	private static final String IS_NEED_BACKUP = "is_need_backup";
	private static final String DEFAULT_API_URL = "default_api_url";
	private static final String DEFAULT_HEP_NODE_URL = "default_hep_node_url";

	private final SharedPreferences pref;

	public SharedPreferenceRepository(Context context) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public String getCurrentWalletAddress() {
		return pref.getString(CURRENT_ACCOUNT_ADDRESS_KEY, null);
	}

	@Override
	public void setCurrentWalletAddress(String address) {
		pref.edit().putString(CURRENT_ACCOUNT_ADDRESS_KEY, address).apply();
	}

	@Override
	public String getDefaultNetwork() {
		return pref.getString(DEFAULT_NETWORK_NAME_KEY, null);
	}

	@Override
	public void setDefaultNetwork(String netName) {
		pref.edit().putString(DEFAULT_NETWORK_NAME_KEY, netName).apply();
	}

	@Override
	public String getWalletName(String address) {
		return pref.getString(address, DEFAULT_WALLET_NAME);
	}

	@Override
	public void setWalletName(String address, String name) {
		pref.edit().putString(address, name).apply();
	}

	@Override
	public int getSharePreferenceLanguageType() {
		return pref.getInt(LANGUAGE_KEY, C.LANGUAGE_DEFAULT_INDEX);
	}

	@Override
	public void setSharePreferenceLange(int languageType) {
		pref.edit().putInt(LANGUAGE_KEY, languageType).apply();
	}

	@Override
	public String getCacheBalance(String address) {
		return pref.getString(CACHE_BALANCE_PREFIX + address, "0");
	}

	@Override
	public void setCacheBalance(String address, String balance) {
		pref.edit().putString(CACHE_BALANCE_PREFIX + address, balance).apply();
	}

	@Override
	public String getIMEI() {
		return pref.getString(C.TOKEN_DEVICE, null);
	}

	@Override
	public void setIMEI(String imei) {
		pref.edit().putString(C.TOKEN_DEVICE, imei).apply();
	}

	@Override
	public boolean isNeedPing() {
		return pref.getBoolean(PING_FLAG, true);
	}

	@Override
	public void setIsNeedPing(boolean isNeedPing) {
		pref.edit().putBoolean(PING_FLAG, isNeedPing).apply();
	}

	@Override
	public boolean isPin() {
		return pref.getBoolean(IS_PIN, false);
	}

	@Override
	public void setIsPin(boolean isPin) {
		pref.edit().putBoolean(IS_PIN, isPin).apply();
	}

	@Override
	public void setIsCheckPin(boolean isCheckPin) {
		pref.edit().putBoolean(IS_CHECK_PIN, isCheckPin).apply();
	}

	@Override
	public String getPinPassword() {
		return pref.getString(PIN_PASSWORD, null);
	}

	@Override
	public void setPinPassword(String password) {
		pref.edit().putString(PIN_PASSWORD, password).apply();
	}

	@Override
	public boolean isSupportFinger() {
		return pref.getBoolean(IS_SUPPORT_FINGER, false);
	}

	@Override
	public void setIsSupportFinger(boolean isSupportFinger) {
		pref.edit().putBoolean(IS_SUPPORT_FINGER, isSupportFinger).apply();
	}

	@Override
	public String getTotalBalance(String address) {
		return pref.getString(CACHE_TOTAL_BALANCE_PREFIX + address, "0");
	}

	@Override
	public void setTotalBalance(String address, String balance) {
		pref.edit().putString(CACHE_TOTAL_BALANCE_PREFIX + address, balance).apply();
	}

	@Override
	public void setInviteCode(String inviteCode) {
		pref.edit().putString(INVITE_DODE, inviteCode).apply();
	}

	@Override
	public void clearPreference(){
		pref.edit().clear().apply();
	}

	@Override
	public boolean isNeedBackup() {
		return pref.getBoolean(IS_NEED_BACKUP, false);
	}

	@Override
	public void setNeedBackUp(boolean isNeedBackup) {
		pref.edit().putBoolean(IS_NEED_BACKUP, isNeedBackup).apply();
	}

	public String getString(String key) {
		return pref.getString(key, null);
	}
}

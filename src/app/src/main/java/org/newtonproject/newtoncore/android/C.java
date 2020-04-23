package org.newtonproject.newtoncore.android;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.meituan.android.walle.WalleChannelReader;

import org.newtonproject.newtoncore.android.utils.Logger;

import java.io.File;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public abstract class C {

    public static final boolean CREATE_WALLET_DIRECT = true;

    private static final String TAG = "C";

    // activity router request code
    public static final int REQUEST_CODE_IMPORT_WALLET = 1001;
    public static final int REQUEST_CODE_SHARE = 1003;
    public static final int REQUEST_CODE_CREATE_WALLET = 1004;
    public static final int REQUEST_CODE_BACKUP = 1005;
    public static final int REQUEST_CODE_VALIDATE = 1006;
    public static final int REQUEST_CODE_FRIEND = 1007;
    public static final int REQUEST_CODE_PAY_SUCCESS = 1008;
    public static final int REQUEST_CODE_UPDATE_FRIEND = 1009;
    public static final int REQUEST_CODE_SET_AMOUNT = 1010;
    public static final int REQUEST_CODE_GET_PIC_URI = 1011;

    // permission code
    public static final int REQUEST_CODE_GET_UNKNOWN_APP_SOURCES = 2001;
    public static final int PERMISSION_CODE_READ_PHONE_STATE = 2002;
    public static final int PERMISSION_CODE_WRITE_EXTERNAL_STORAGE = 2003;
    public static final int PERMISSION_CODE_INSTALL_PACKAGES = 2004;
    //router type
    public static final int TERMS_TYPE = 3001;
    public static final int POLICY_TYPE = 3002;

    // language index
    public static final int LANGUAGE_DEFAULT_INDEX = 0;
    public static final int LANGUAGE_ENGLISH_INDEX = 1;
    public static final int LANGUAGE_CHINESE_INDEX = 2;
    public static final int LANGUAGE_JAPANESE_INDEX = 3;

    // derive path
    public static final String DERIVE_PATH_NEW = "m/44'/1642'/0'/0/0";

    //new network config.
    public static final String NEWCHAIN_TESTNET = "NewChain TestNet";
    public static final String ISSAC_UNIT = "ISAAC";

    public static final long GAS_LIMIT_MIN = 21000L;
    public static final long GAS_LIMIT_MAX = 300000L;
    public static final long GAS_PRICE_MIN = 1000000000L;
    public static final long NETWORK_FEE_MAX = 20000000000000000L;
    public static final int ETHER_DECIMALS = 18;
    public static final String NEW_SYMBOL = "NEW";
    public static final String NEWTON_SYMBOL = "newton";
    public static final String GWEI_UNIT = "Gwei";

    // extra data for intent
    public static final String EXTRA_ADDRESS = "ADDRESS";
    public static final String EXTRA_CONTRACT_ADDRESS = "CONTRACT_ADDRESS";
    public static final String EXTRA_DECIMALS = "DECIMALS";
    public static final String EXTRA_SYMBOL = "SYMBOL";
    public static final String EXTRA_SENDING_TOKENS = "SENDING_TOKENS";
    public static final String EXTRA_TO_ADDRESS = "TO_ADDRESS";
    public static final String EXTRA_AMOUNT = "AMOUNT";
    public static final String EXTRA_GAS_PRICE = "GAS_PRICE";
    public static final String EXTRA_GAS_LIMIT = "GAS_LIMIT";
    public static final String EXTRA_MNEMONIC = "MNEMONIC";
    public static final String EXTRA_PASSWORD = "PASSWORD";
    public static final String EXTRA_NAME = "NAME";
    public static final String EXTRA_TXID = "TXID";
    public static final String EXTRA_URL = "URL";
    public static final String EXTRA_TITLE = "TITLE";
    public static final String EXTRA_PAY_REQUEST_SOURCE = "REQUEST_PAY_SOURCE";
    public static final String EXTRA_AUTH_TYPE = "AUTH_TYPE";
    public static final String LANGUAGE_SOURCE_IS_GUIDE = "LANGUAGE_SOURCE";
    public static final String EXTRA_PUBLICKEY = "PUBLIC_KEY";
    public static final String EXTRA_SIGN_R = "SIGN_R";
    public static final String EXTRA_SIGN_S = "SIGN_S";
    public static final String EXTRA_CONFIRM_TITLE = "EXTRA_CONFIRM_TITLE";
    public static final String EXTRA_CONFIRM_CONTENT = "EXTRA_CONFIRM_CONTENT";
    public static final String EXTRA_CONFIRM_BUTTON_TEXT = "EXTRA_CONFIRM_BUTTON";
    public static final String EXTRA_CONFIRM_ICON = "EXTRA_ICON_RESOURCE";

    // app dir
    public static final String CONTACT_DIR = App.getAppContext().getCacheDir() + File.separator + "newpay-contact";
    public static final String HEP_CACHE = App.getAppContext().getCacheDir() + "/hep-cache";
    public static final String TRANSACTION_CACHE = App.getAppContext().getCacheDir() + "/transaction-cache";

    // Precision.
    public static final int NEW_PRECISION = 2;
    // cache time
    public static final int CACHE_TIME_LONG = 3;

    // global config
    public static final long API_TIMEOUT = 30L;
    public static final String LIMIT = "50";
    public static final String FRIEND_SOURCE = "friend_source";
    public static final String NewExplorer = "NewExplorer";


    public static final String DOWNLOAD_FILE_FOLDER = "newpay";
    public static final String DOWNLOAD_FILE_NM_PREFIX = "newpay-android";
    public static final int DATABASE_VERSION = 3;

    // request common params
    public static final String TOKEN_DEVICE = "device_id";

    public static final String APP_ENV = getMetaData("APP_ENV");
    public static final int CURRENT_NET = getNetByEnv(APP_ENV);

    // request url, chain , api and explore.
    public static final String USER_OF_TERMS_URL = "https://www.newtonproject.org/term-of-service/";

    // beta net
    public static final String RPC_URL_BETANET = "https://rpc1.newchain.newtonproject.org";
    private static final String EXPLORE_URL_BETANET = "http://explorer.newtonproject.beta.diynova.com/";
    private static final int CHAIN_ID_BETANET = 1007;

    // dev net
    private static final String RPC_URL_DEV = "https://devnet.newchain.cloud.diynova.com";
    private static final String EXPLORE_URL_DEV = "http://explorer.newtonproject.dev.diynova.com/";
    private static final int CHAIN_ID_DEVNET = 1002;

    // test net
    private static final String RPC_TESTNET = "https://rpc3.newchain.cloud.diynova.com";
    private static final String EXPLORER_TESTNET = "https://explorer.testnet.newtonproject.org/";

    private static final int CHAIN_ID_TESTNET = 1007;

    // main net,
    private static final String RPC_MAINNET = "https://jp.rpc.mainnet.newtonproject.org/";
    private static final String EXPLORER_MAINNET = "https://explorer.newtonproject.org/";
    private static final int CHAIN_ID_MAINNET = 1012;

    // external network config
    public static final String RPC2 = "https://rpc2.newchain.cloud.diynova.com";
    // is check back up flag
    public static boolean IS_CHECK_BACKUP = true;

    // server config in current application.
    public static final String CURRENT_RPC_URL = getRPCURL(CURRENT_NET);
    public static final String CURRENT_EXPLORE_URL = getExploreUrl(CURRENT_NET);

    public static final int CURRENT_CHAIN_ID = getChainId(CURRENT_NET);

    private static int getNetByEnv(String appEnv) {
        int net = 0;
        switch (appEnv) {
            case "testnet":
                net = NET_TYPE.TESTNET;
                break;
            case "beta":
                net = NET_TYPE.BETANET;
                break;
            case "dev":
                net = NET_TYPE.DEVNET;
                break;
            case "product":
                net = NET_TYPE.MAINNET;
                break;
            default:
                net = NET_TYPE.TESTNET;
                break;
        }
        return net;
    }


    private static String getMetaData(String key) {
        String result = null;
        try {
            ApplicationInfo application = App.getAppContext().getPackageManager().getApplicationInfo(App.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            result = application.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Logger.d(TAG, "getMetaData key is:" + key + "\r\n result:" + result );
        return result;
    }

    private static int getChainId(int currentNet) {
        int chainId = 16888;
        switch (currentNet) {
            case NET_TYPE.MAINNET:
                chainId = CHAIN_ID_MAINNET;
                break;
            case NET_TYPE.TESTNET:
                chainId = CHAIN_ID_TESTNET;
                break;
            case NET_TYPE.DEVNET:
                chainId = CHAIN_ID_DEVNET;
                break;
            case NET_TYPE.BETANET:
                chainId = CHAIN_ID_BETANET;
                break;
            default:
                chainId = CHAIN_ID_TESTNET;
                break;
        }
        return chainId;
    }

    private static String getRPCURL(int currentNet) {
        String rpcUrl = null;
        switch (currentNet) {
            case NET_TYPE.MAINNET:
                rpcUrl = RPC_MAINNET;
                break;
            case NET_TYPE.TESTNET:
                rpcUrl = RPC_TESTNET;
                break;
            case NET_TYPE.DEVNET:
                rpcUrl = RPC_URL_DEV;
                break;
            case NET_TYPE.BETANET:
                rpcUrl = RPC_URL_BETANET;
                break;
            default:
                rpcUrl = RPC_TESTNET;
                break;
        }
        return rpcUrl;
    }


    private static String getExploreUrl(int currentNet) {
        String exploreUrl = null;
        switch (currentNet) {
            case NET_TYPE.MAINNET:
                exploreUrl = EXPLORER_MAINNET;
                break;
            case NET_TYPE.TESTNET:
                exploreUrl = EXPLORER_TESTNET;
                break;
            case NET_TYPE.DEVNET:
                exploreUrl = EXPLORE_URL_DEV;
                break;
            case NET_TYPE.BETANET:
                exploreUrl = EXPLORE_URL_BETANET;
                break;
            default:
                exploreUrl = EXPLORER_TESTNET;
                break;
        }
        return exploreUrl;
    }

    public interface ErrorCode {
        int UNKNOWN = 1;
        int CANT_GET_STORE_PASSWORD = 2;
    }

    public interface Key {
        String WALLET = "wallet";
        String TRANSACTION = "transaction";
        String CONTACTS = "contacts";
    }

    public interface NET_TYPE {
        int MAINNET = 0;
        int TESTNET = 1;
        int BETANET = 3;
        int DEVNET = 2;
    }

    public interface TRANSACTION_STATUS {
        int SEND = 0;
        int RECEIVE = 1;
        int MOVED = 2;
    }


    public interface LOCKED_TYPE {
        String VOTE = "VOTE";
        String NODE = "NODE";
        String PENDING = "PENDING";
    }

}

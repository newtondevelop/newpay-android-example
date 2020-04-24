package org.newtonproject.newtoncore.android.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.newtonproject.newtoncore.android.App;
import org.newtonproject.newtoncore.android.BuildConfig;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.ContactsRepository;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.web3j.utils.Numeric;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class StringUtil {

    private static ContactsRepository respository;

    public static String getString(int stringId){
        return App.getAppContext().getString(stringId);
    }

    public static ArrayList<String> getMnemonicFromString(String mnemonic) {
        ArrayList<String> list = new ArrayList<>();
        if (null == mnemonic || mnemonic.length() == 0) {
            return null;
        }
        mnemonic = mnemonic.replace("\n", " ").replace("\r"," ");
        String[] strArray = mnemonic.split(" ");
        for (String str : strArray) {
            if(!TextUtils.isEmpty(str)) {
                list.add(str.trim());
            }
        }
        return list;
    }

    public static String getMnemonicFromList(List<String> mnemonic) {
        StringBuilder builder = new StringBuilder();
        for(String str : mnemonic) {
            builder.append(str + " ");
        }
        return builder.toString().trim();
    }


    public static String getContactName(String walletAddress, String address) {
        if(respository == null) {
            respository = new ContactsRepository();
        }
        ContactsInfo contacts = respository.getContacts(new Wallet(NewAddressUtils.newAddress2HexAddress(walletAddress)), address);
        if(null != contacts) {
            return contacts.name;
        }
        return null;
    }

    public static String getCheckedMd5(String md5) {
        if(null == md5) return null;
        int len = md5.length();
        StringBuilder builder = new StringBuilder();
        if(len < 32) {
            int diff = 32 - len;
            for(int i = 0; i < diff; i++) {
                builder.append("0");
            }
        }
        return builder.append(md5).toString();
    }

    public static String getTimeStamp() {
        long time = System.currentTimeMillis();
        return String.valueOf(time);
    }

    public static String getNonce() {
        byte init[] = new byte[16];
        SecureRandomUtils.secureRandom().nextBytes(init);
        return Numeric.toHexString(init);
    }

    public static String getWalletName(Context context, String address) {
        SharedPreferenceRepository preferenceRepository = new SharedPreferenceRepository(context);
        return preferenceRepository.getWalletName(address);
    }

    public static String adjustPublicKey(String pub) {
        StringBuilder res = new StringBuilder(Numeric.cleanHexPrefix(pub));
        int length = res.length();
        for(int i = 0; i < 128 - length; i ++) {
            res.insert(0, "0");
        }
        return Numeric.prependHexPrefix(res.toString());
    }

    public static void copyContent(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) App.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copy", content);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(context, getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show();
    }

    public static String getTransactionError(String message) {
        if(!TextUtils.isEmpty(message)) {
            Logger.e("error", message);
            if(TextUtils.equals(message, "Internal Error - 428")) {
                message = getString(R.string.transaction_error_428);
            } else if(TextUtils.equals(message, "Internal Error - 420")) {
                message = getString(R.string.transaction_error_420);
            }
        }
        return message;
    }

    public static String formatAmount(String amount) {
        if(TextUtils.isEmpty(amount)) return null;
        DecimalFormat format = new DecimalFormat(",###.00");
        String res = BalanceUtils.divideZero(format.format(Double.valueOf(amount)));
        return TextUtils.isEmpty(res) ? "0" : res;
    }

    public static Gson getGson() {
        return new GsonBuilder().
                serializeNulls()
                .disableHtmlEscaping()
                .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue())
                            return new JsonPrimitive(src.longValue());
                        return new JsonPrimitive(src);
                    }
                }).create();
    }

    public static Gson getGsonWithoutNull() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue())
                            return new JsonPrimitive(src.longValue());
                        return new JsonPrimitive(src);
                    }
                }).create();
    }

    public static String getCurrentUserAgent(Context context) {
            return "NewPay/" +
                    BuildConfig.VERSION_NAME + "-" +
                    BuildConfig.VERSION_CODE +
                    ";(" +
                    PhoneUtils.getMobileInfo(context) + "-" +
                    Build.VERSION.SDK_INT + "-" +
                    PhoneManager.getSessionID(context) + "-" +
                    ";Android;" +
                    LanguageUtils.getCurrentLanguageFlag(context) +
                    ")";
    }
}

package org.newtonproject.newtoncore.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import org.newtonproject.newtoncore.android.App;
import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.views.account.WelcomeActivity;
import org.newtonproject.newtoncore.android.views.home.HomeActivity;

import java.util.Locale;
import java.util.TimeZone;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class LanguageUtils {
    public static void setLocale(Context context) {
        int type = getLanguageType(context);
        setLocale(context, type);
    }

    public static void setLocale(Context context, int type) {
        setCurrentLocale(context, type);
        setCurrentLocale(App.getAppContext(), type);
    }

    private static void setCurrentLocale(Context context, int type) {
        // fix bug for webview can not change language.
        new WebView(context).destroy();
        // change language
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = getLocaleByType(type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(config.locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.createConfigurationContext(config);
            Locale.setDefault(config.locale);
        }
        setDefaultLanguage(config.locale.getLanguage(), context);
        resources.updateConfiguration(config, dm);
    }


    public static boolean isSameLanguage(Context context) {
        int type = getLanguageType(context);
        return isSameLanguage(context, type);
    }

    public static boolean isSameLanguage(Context context, int type) {
        Locale locale = getLocaleByType(type);
        Locale appLocale = context.getResources().getConfiguration().locale;
        return locale.equals(appLocale);
    }

    private static Locale getLocaleByType(int type) {
        Locale locale;
        switch (type) {
            case C.LANGUAGE_DEFAULT_INDEX:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    LocaleList localeList = LocaleList.getDefault();
                    int spType = getLanguageType(App.getAppContext());
                    if (spType != 0 && localeList.size() > 1) {
                        // user changed language.
                        locale = localeList.get(1);
                    } else {
                        // default language
                        locale = localeList.get(0);
                    }
                } else {
                    locale = Locale.getDefault();
                }
                break;
            case C.LANGUAGE_ENGLISH_INDEX:
                locale = Locale.ENGLISH;
                break;
            case C.LANGUAGE_CHINESE_INDEX:
                locale = Locale.CHINESE;
                break;
            case C.LANGUAGE_JAPANESE_INDEX:
                locale = Locale.JAPAN;
                break;
            default:
                locale = Locale.ENGLISH;
                break;
        }
        return locale;
    }

    private static int getLanguageType(Context context) {
        return new SharedPreferenceRepository(context).getSharePreferenceLanguageType();
    }

    public static void restartActvity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void restartNewIdActvity(Context context) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private static void setDefaultLanguage(String defaultLanguage, Context context) {
        if(defaultLanguage.contains("zh")) {
            new SharedPreferenceRepository(context).setSharePreferenceLange(C.LANGUAGE_CHINESE_INDEX);
        }else if(defaultLanguage.contains("ja")){
            new SharedPreferenceRepository(context).setSharePreferenceLange(C.LANGUAGE_JAPANESE_INDEX);
        } else {
            new SharedPreferenceRepository(context).setSharePreferenceLange(C.LANGUAGE_ENGLISH_INDEX);
        }
    }

    public static String getNSTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName(false, TimeZone.SHORT);
    }

    public static String getLanguage(int id) {
        String language = null;
        if(id == C.LANGUAGE_ENGLISH_INDEX) {
            language = "en";
        } else if(id == C.LANGUAGE_CHINESE_INDEX) {
            language = "zh";
        } else if(id == C.LANGUAGE_JAPANESE_INDEX){
            language = "ja";
        }else{
            language = "en";
        }
        return language;
    }

    public static String getCurrentLanguage(Context context) {
        String language;
        setLocale(context);
        int languageType = getLanguageType(context);
        if(languageType == C.LANGUAGE_ENGLISH_INDEX) {
            language = "English";
        } else if(languageType == C.LANGUAGE_CHINESE_INDEX) {
            language = "简体中文";
        } else if(languageType == C.LANGUAGE_JAPANESE_INDEX){
            language = "日本语";
        }else{
            language = "English";
        }
        return language;
    }

    static String getCurrentLanguageFlag(Context context) {
        setLocale(context);
        int languageType = getLanguageType(context);
        return getLanguage(languageType);
    }
}
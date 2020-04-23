package org.newtonproject.newtoncore.android.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class DateUtils {

    public static String getDateTime(long timeStampInSec) {
        Date date = new Date(timeStampInSec * 1000);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time.format(date);
    }

    public static String getDateTimeWithoutYear(long timeStampInSec) {
        Date date = new Date(timeStampInSec * 1000);
        SimpleDateFormat time = new SimpleDateFormat("MM-dd HH:mm");
        return time.format(date);
    }

    public static String getHourTime(long timeStampInSec) {
        Date date = new Date(timeStampInSec * 1000);
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        return time.format(date);
    }

    public static String getMiniuteTime(long timeStampInSec) {
        Date date = new Date(timeStampInSec * 1000);
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        return time.format(date);
    }

    public static String getDateShortTime(long timeStampInSec) {
        Date date = new Date(timeStampInSec * 1000);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        return time.format(date);
    }

    public static String getDateTimeStandard(long timeStampInSec) {
        Date date = new Date(timeStampInSec);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time.format(date);
    }

    public static String getDateTimeMinute(long timeStampInSec) {
        Date date = new Date(timeStampInSec);
        SimpleDateFormat time = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return time.format(date);
    }


}

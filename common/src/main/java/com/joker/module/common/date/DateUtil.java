package com.joker.module.common.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joker on 2017/4/30.
 */
public class DateUtil {
    public static String TIMESTAMP_UUID_FORMAT = "YYYYMMddHHmmssSSS";

    public static String timeStampUUID(String format) {
        if (null == format) {
            format = TIMESTAMP_UUID_FORMAT;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date());
    }
}

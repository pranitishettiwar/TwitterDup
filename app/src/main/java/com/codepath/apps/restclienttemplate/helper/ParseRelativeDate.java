package com.codepath.apps.restclienttemplate.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateUtils;

/**
 * Created by praniti on 9/29/17.
 */

public class ParseRelativeDate {

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        //        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        //        long timeNow = Long.valueOf(sf.format(new Date(System.currentTimeMillis())));

        String relativeDate = "";

        try {
            Date date = sf.parse(rawJsonDate);
            long dateMillis = date.getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils
                .SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}

package org.roysin.luckymoney.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/1/11.
 */
public class LogUtils {
    public static   final String TAG = "LuckyMoneyDebug";
    public static void log(String msg){
        Log.d(TAG,msg);
    }
    public static void log(String tag, String msg){
        Log.d(tag ,msg);
    }
}


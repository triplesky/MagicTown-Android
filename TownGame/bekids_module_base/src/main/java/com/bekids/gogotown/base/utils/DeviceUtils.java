package com.bekids.gogotown.base.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.StatFs;
import android.telephony.TelephonyManager;



/**
 * Author: LuckyFind
 * Date: 2021/3/26
 * Desc:
 */
public class DeviceUtils {

    /**
     * 获取内部存储空间的可用大小
     * */
    public  static int getInternalAvailableMemorySize(Context context){
        StatFs statFs = new StatFs(context.getFilesDir().getAbsolutePath());
        return (int) (statFs.getAvailableBytes()/1204/1024);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}

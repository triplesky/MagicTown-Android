package com.bekids.gogotown.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

/**
 ***************************************
 * 项目名称: Town Game
 * @Author ll
 * 创建时间: 2023/10/21    16:35
 * 用途
 ***************************************

 */
object PackageUtil {
     fun getAllLauncherIconPackages(context: Context): MutableList<String> {
        val launcherIconPackageList= mutableListOf<String>()
        val intent = Intent()
        intent.setAction(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        //set MATCH_ALL to prevent any filtering of the results
        val resolveInfos: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        for (info in resolveInfos) {
            launcherIconPackageList.add(info.activityInfo.packageName)
        }
        return launcherIconPackageList
    }
}
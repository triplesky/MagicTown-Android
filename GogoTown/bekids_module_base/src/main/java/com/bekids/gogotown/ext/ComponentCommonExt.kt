package com.bekids.gogotown.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Process
import android.provider.MediaStore
import android.text.TextUtils


import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.system.exitProcess







/**
 * 判断设备是否安装微信APP
 *
 * @return boolean
 */
fun Context.isWeixinAvilible(): Boolean {
    val pinfo = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
    if (pinfo != null) {
        for (i in pinfo.indices) {
            val pn = pinfo[i].packageName
            if ("com.tencent.mm" == pn) {
                return true
            }
        }
    }
    return false
}


/**
 * 是否有网络连接
 * @receiver Context
 * @return Boolean
 */
@SuppressLint("MissingPermission")
fun Context.isNetworkAvailable(): Boolean {
    val mNetworkInfo = connectivityManager?.activeNetworkInfo
    if (mNetworkInfo != null) return mNetworkInfo.isAvailable
    return false
}

/**
 * 是否有运行的服务
 * @receiver Context
 * @param className String
 * @return Boolean
 */
fun Context.isServiceRunning(className: String): Boolean {
    var isRunning = false
    val serviceList = activityManager?.getRunningServices(30)
    if (serviceList == null || serviceList.size <= 0) {
        return false
    }
    for (i in serviceList.indices) {
        if (serviceList[i] != null && serviceList[i].service != null) {
            val cName = serviceList[i].service.className
            if (cName.contains(className)) {
                isRunning = true
                break
            }
        }
    }
    return isRunning
}


/**
 * 包名判断是否为主进程
 *
 * @param context
 * @return
 */
fun Context.isMainProcess(): Boolean {
    return packageName == getProcessName()
}

/**
 * 获取进程名称
 *
 * @param context
 * @return
 */
fun Context.getProcessName(): String? {
    try {
        val runningApps = activityManager?.runningAppProcesses ?: return null
        for (proInfo in runningApps) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * 判断当前app是否存活
 * @receiver Context
 * @param packageName String
 * @return Boolean
 */
fun Context.isAppAlive(packageName: String): Boolean {
    val processInfos = activityManager?.runningAppProcesses
    if (processInfos != null) {
        for (i in processInfos.indices) {
            if (processInfos[i].processName == packageName) {
                return true
            }
        }
    }
    return false
}



/**
 * 图片存放本地
 * @receiver Context
 * @param bmp Bitmap?
 * @param fileName String?
 */
fun Context.saveImageToGallery(bmp: Bitmap?, fileName: String?) {
    var fileName = fileName
    if (this == null || bmp == null) {
        return
    }
    val dirName = "parent_Doc"
    val appDir = File(this.getExternalCacheDir(), dirName)
    if (!appDir.exists()) {
        appDir.mkdir()
    }
    if (TextUtils.isEmpty(fileName)) {
        fileName = System.currentTimeMillis().toString() + ".jpg"
    }
    val file = File(appDir, fileName)
    try {
        val fos = FileOutputStream(file)
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    } catch (e: FileNotFoundException) {
        toast("保存失败")
        e.printStackTrace()
    } catch (e: IOException) {
        toast("保存失败")
        e.printStackTrace()
    }
    // 把文件插入到系统图库
    try {
        val insertImage: String = MediaStore.Images.Media.insertImage(this.contentResolver,
                file.absolutePath, fileName, null)
        //            // 通知图库更新
        val file1 = File(getRealPathFromURI(Uri.parse(insertImage)))
        val intent = Intent()
        intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
        intent.data = Uri.fromFile(file1)
        this.sendBroadcast(intent)
        toast("保存成功")
    } catch (e: java.lang.Exception) {
        toast("保存失败")
        e.printStackTrace()
    }
}

fun Context.getRealPathFromURI(contentUri: Uri): String? {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = this.contentResolver.query(contentUri, proj, null, null, null)
    val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor?.moveToFirst()
    column_index?.let {
        val fileStr = cursor.getString(column_index)
        cursor.close()
        return fileStr
    }
    return null
}

/**
 * 清楚当前所有的activity
 */
fun Context.clearAllActivity() {
    val allActivitys = getAllActivitys()
    for (i in allActivitys.indices) {
        allActivitys.get(i).finish()
    }
}

/**
 * 获取当前所有的activity
 *
 * @return activity列表
 */
@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
fun Context.getAllActivitys(): List<Activity> {
    val list = ArrayList<Activity>()
    try {
        val activityThread = Class.forName("android.app.ActivityThread")
        val currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread")
        currentActivityThread.isAccessible = true
        //获取主线程对象
        val activityThreadObject = currentActivityThread.invoke(null)
        val mActivitiesField = activityThread.getDeclaredField("mActivities")
        mActivitiesField.isAccessible = true
        val mActivities = mActivitiesField.get(activityThreadObject) as Map<Any, Any>
        for ((_, value) in mActivities) {
            val activityClientRecordClass = value.javaClass
            val activityField = activityClientRecordClass.getDeclaredField("activity")
            activityField.isAccessible = true
            val o = activityField.get(value)
            list.add(o as Activity)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return list
}

/**
 * 注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
 */
fun Context.killAppProcess() {
    val mList = activityManager?.runningAppProcesses
    for (runningAppProcessInfo in mList!!) {
        if (runningAppProcessInfo.pid != Process.myPid()) {
            Process.killProcess(runningAppProcessInfo.pid)
        }
    }
    Process.killProcess(Process.myPid())
    exitProcess(0)
}




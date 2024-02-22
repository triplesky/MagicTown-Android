package com.bekids.gogotown.base.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.bekids.gogotown.base.BaseApplication
import com.tencent.mmkv.MMKV
import java.io.File

/**
 ***************************************
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:58 下午
 * 用途
 ***************************************

 */
object MMKvUtils {
    const val DIR_NAME = "/bekidsdir"
    const val DATA_NAME = "bekidsdata"
    private var mmkv: MMKV? = null

    fun initMMKV() {
        if (mmkv != null) {
            return
        }
        var appfilePath = BaseApplication.instance.filesDir
        if (appfilePath == null) {
            appfilePath = File("/data/data/" +  BaseApplication.instance.packageName + "/files")
            appfilePath.mkdirs()
        }

        // 设置初始化的根目录
        try {
            val dir = appfilePath.toString() + DIR_NAME
            MMKV.initialize(dir)
            if (mmkv == null) {
                mmkv = MMKV.mmkvWithID(DATA_NAME)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mmkv = null
        }
    }

    /**
     * 将 sp 导入到 mmkv 中并清空
     *
     * @param context
     * @param name
     */
    fun importSpToMmkv(context: Context, name: String?) {
        val oldSp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        importSpToMmkv(oldSp)
    }

    /**
     * 将 sp 导入到 mmkv 中
     *
     * @param oldSp
     */
    fun importSpToMmkv(oldSp: SharedPreferences?) {
        try {
            if (mmkv != null) {
                mmkv?.importFromSharedPreferences(oldSp)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T : Any> put(key: String, value: T?) {
        if (value == null) {
            return
        }
        if (mmkv == null) {
            initMMKV()
        }

        if (value is String) {
            mmkv?.putString(key, value)
        } else if (value is Int) {
            mmkv?.putInt(key, value)
        } else if (value is Boolean) {
            mmkv?.putBoolean(key, value)
        } else if (value is Float) {
            mmkv?.putFloat(key, value)
        } else if (value is Long) {
            mmkv?.putLong(key, value)
        } else {
            mmkv?.putString(key, value.toString())
        }
    }

    operator fun <T : Any> get(key: String, defaultObject: T?): T? {
        if (mmkv == null) {
            initMMKV()
        }


        if (defaultObject is String) {
            return mmkv!!.getString(key, defaultObject) as? T ?: defaultObject
        } else if (defaultObject is Int) {
            return mmkv?.getInt(key, defaultObject)  as? T ?: defaultObject
        } else if (defaultObject is Boolean) {
            return mmkv?.getBoolean(key, defaultObject)  as? T ?: defaultObject
        } else if (defaultObject is Float) {
            return mmkv?.getFloat(key, defaultObject)  as? T ?: defaultObject
        } else if (defaultObject is Long) {
            return mmkv?.getLong(key, defaultObject) as? T ?: defaultObject
        }
        return defaultObject
    }

    fun remove(key: String?) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        if (mmkv == null) {
            initMMKV()
        }
        mmkv?.removeValueForKey(key)
    }

    operator fun contains(key: String): Boolean {
        if (TextUtils.isEmpty(key)) {
            return false
        }
        if (mmkv == null) {
            initMMKV()
        }
        return mmkv?.containsKey(key) ?: false
    }
}
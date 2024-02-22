package com.bekids.gogotown.base.utils

import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

/**
 ***************************************
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    3:02 下午
 * 用途
 ***************************************

 */
object MMKVImpl {
    @Volatile
    var transformIng: AtomicBoolean = AtomicBoolean(false)

    @Volatile
    var transformOk = AtomicBoolean(false)

    private const val TRANSFORM_RECORD = "transform_record"

    /**
     * 获取存放数据
     * @return 值
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getValue(key: String, default: T): T {
        Log.e("ll", "从mmkv 获得数据$key")
        return MMKvUtils[key, default] ?: default
    }

    fun getString(key: String, default: String = ""): String {
        return getValue(key, default)
    }

    fun getInt(key: String, default: Int = 0): Int {
        return getValue(key, default)
    }

    fun getLong(key: String, default: Long = 0): Long {
        return getValue(key, default)
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return getValue(key, default)
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return getValue(key, default)
    }

    /**
     * 存放SharedPreferences
     * @param key 键
     * @param value 值
     */
    fun saveValue(key: String, value: Any) {
        Log.e("ll", "mmkv 中存放数据     $key    $value")
        MMKvUtils.put(key, value)
    }

    /**
     * 是否包含
     */
    fun contains(key: String): Boolean {
        return MMKvUtils.contains(key)
    }
}
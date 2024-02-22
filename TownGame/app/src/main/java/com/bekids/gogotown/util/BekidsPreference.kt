package com.bekids.gogotown.util

import android.annotation.SuppressLint
import com.bekids.gogotown.base.utils.MMKvUtils
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 ***************************************
 * 项目名称: enlighten
 * @Author ll
 * 创建时间: 2022/7/21    4:06 下午
 * 用途
 ***************************************

 */
class BekidsPreference<T:Any>(val name: String, private val default: T) :
    ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putValue(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putValue(name: String, value: T){
        MMKvUtils.put(name, value)
    }

    @Suppress("UNCHECKED_CAST")
    fun getValue(name: String, default: T): T{
        return MMKvUtils[name, default] ?: default
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        return MMKvUtils.contains(key)
    }
}
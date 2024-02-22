package com.bekids.gogotown.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/7/25    10:51 上午
 * 用途
 ***************************************

 */
abstract class BaseApplication : MultiDexApplication() {

    companion object {
        lateinit var instance : BaseApplication

        lateinit var context : Context

        lateinit var application: Application

        var token_invalid = 0 //0有效，1失效
    }
}
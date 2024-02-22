package com.bekids.gogotown.router

/**
 ***************************************
 * 项目名称: enlighten
 * @Author ll
 * 创建时间: 2022/7/21    3:44 下午
 * 用途
 ***************************************

 */
object RouterDelegate {
    private var config: RouterConfig? = null
    private var isInit: Boolean = false

    fun init(config: RouterConfig) {
        if (isInit) {
            return
        }
        RouterDelegate.config = config
        isInit = true
    }

    fun getCurrentActivityStatisticName(): String? {
        return checkConfig().getCurrentActivityStatisticName()
    }

    fun getCurrentActivityStatisticKey(): String? {
        return checkConfig().getCurrentActivityStatisticKey()
    }

    interface RouterConfig {
        fun getCurrentActivityStatisticKey(): String?
        fun getCurrentActivityStatisticName(): String?
    }


    fun checkConfig(): RouterConfig {
        if (config == null) {
            throw RuntimeException("请先调用 RouterDelegate.init(config: RouterConfig) 初始化路由代理")
        }

        return config!!
    }
}
package com.bekids.gogotown.router

import com.alibaba.android.arouter.launcher.ARouter

/**
 ***************************************
 * 项目名称: enlighten
 * @Author ll
 * 创建时间: 2022/7/21    3:45 下午
 * 用途
 ***************************************

 */
object BekidsRouter {
    private fun BekidsRouter() {
        //no instance
    }


    fun build(path: String): BekidsPostcard {
        return BekidsPostcard(path)
    }

    /**
     * 第三方的router
     *
     * @return 第三方路由
     */
    fun getThirdRouter(): ARouter? {
        return ARouter.getInstance()
    }
}
package com.bekids.gogotown.router

/**
 ***************************************
 * 项目名称: enlighten
 * @Author ll
 * 创建时间: 2022/7/21    3:37 下午
 * 用途
 ***************************************

 */
object RouterHelper {
    fun mainActivity() = BekidsRouter.build(RouterPath.Main.MAIN_ACTIVITY).navigation()

    fun loginActivity(): Any? = BekidsRouter.build(RouterPath.Login.LOGIN_PAGE).navigation()
}
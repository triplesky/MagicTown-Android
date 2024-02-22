package com.bekids.gogotown.router

/**
 ***************************************
 * 项目名称: enlighten
 * @Author ll
 * 创建时间: 2022/7/21    3:38 下午
 * 用途
 ***************************************

 */
class RouterPath {
    object Login {
        const val BK_LOGIN_CENTER = "/login_center/"
        const val LOGIN_PAGE = BK_LOGIN_CENTER + "/login_page/"
    }
    object Main{
        const val BK_MAIN_CENTER = "/main_center/"
        const val MAIN_ACTIVITY = BK_MAIN_CENTER + "main_activity"
    }
}
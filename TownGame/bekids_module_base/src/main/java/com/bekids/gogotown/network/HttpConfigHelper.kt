package com.bekids.gogotown.network

import android.util.Log
import com.bekids.gogotown.base.utils.MMKVImpl

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:47 下午
 * 用途
 ***************************************

 */
object HttpConfigHelper{
    /////////////////不同环境的 baseurl///////////////////////////
    /**
     * 轻课baseurl各个环境
     */
    private const val TEST_SERVER_URL = "https://staging-api.ihumand.com"
    private const val DEVELOP_SERVER_URL = "https://staging-api.ihumand.com"
    private const val STAGING_SERVER_URL = "https://staging-api.ihumand.com"
    private const val PRODUCT_SERVER_URL = "https://townapi.bekids.com"


    private const val STAGING_H5_URL = "https://static.ihumand.com/"
    private const val PRODUCT_H5_URL = "https://static.ihumand.com/"

    private const val STAGING_H5_PATH = "staging"
    private const val PRODUCT_H5_PATH = "h5"




    //课程详情(未加入）
     var H5_COURSE_DETAIL_NOT_IN = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/course-detail/"
    //课程详情(加入）
     var H5_COURSE_DETAIL_IN = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/plan-detail/"

    //全部课程
     var H5_ALL_COURSE_LIST = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/course-list"

    //成长计划
     var H5_GROW_PLAN = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/growth-plan?appid="
    //家长奖励记录
     var H5_PRIZE_RECORD = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/prize-record?appid="

    //家长更多奖励
     var H5_PRIZE_MORE = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/prize-more?appid="

    //积分兑换
     var H5_CREDIT_EXCHANGE = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/credit-exchange"

    //积分记录
     var H5_CREDIT_RECORD = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/credit-record"

    //解锁卡
     var H5_UNLOCK_CARD = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/unlock-card"

    //我的互动
     var H5_INTERACTION = "${getH5HostUrlByEnv()}${getH5PathUrlByEnv()}/parent/circle/#/interaction"




    //测试
    const val ENV_DEBUG = 0

    //开发
    const val ENV_DAPI = 1

    //预上线
    const val ENV_STAGING = 2

    //线上
    const val ENV_RELEASE = 3

    const val ENV_KEY = "ENV"
    ////////////////////////////////////////////

    fun isReleaseEnv(): Boolean {
        return env() == ENV_RELEASE
    }

    fun initConfig(e: Int) {
        env = e
        MMKVImpl.saveValue(ENV_KEY, e)
    }

    var env: Int = 2






    fun baseUrl(): String {
        Log.d(javaClass.simpleName, "baseUrl: " + getUrlByEnv())
        return getUrlByEnv()
    }

    fun isOpenLogging(): Boolean {
        return env != ENV_RELEASE
    }


    fun env(): Int {
        Log.d(javaClass.simpleName, "env: $env")
        return env
    }



     fun getH5HostUrlByEnv() : String =
        when (env()) {
            //
            ENV_DEBUG ->
                STAGING_H5_URL
            ENV_DAPI ->
                STAGING_H5_URL
            ENV_STAGING ->
                STAGING_H5_URL
            ENV_RELEASE ->
                PRODUCT_H5_URL
            else ->
                PRODUCT_H5_URL
        }

    private fun getH5PathUrlByEnv() : String =
        when (env()) {
            //
            ENV_DEBUG ->
                STAGING_H5_PATH
            ENV_DAPI ->
                STAGING_H5_PATH
            ENV_STAGING ->
                STAGING_H5_PATH
            ENV_RELEASE ->
                PRODUCT_H5_PATH
            else ->
                PRODUCT_H5_PATH
        }

    /**
     * 根据环境返回对应的url
     */
    private fun getUrlByEnv(): String =
        when (env()) {
            //
            ENV_DEBUG ->
                TEST_SERVER_URL
            ENV_DAPI ->
                DEVELOP_SERVER_URL
            ENV_STAGING ->
                STAGING_SERVER_URL
            ENV_RELEASE ->
                PRODUCT_SERVER_URL
            else ->
                PRODUCT_SERVER_URL
        }


    fun getAppId(): String =
        when (env()) {
            ENV_DEBUG, ENV_DAPI -> {
                "112233"
            }
            ENV_STAGING, ENV_RELEASE -> {
                "528613"
            }
            else -> "528613"
        }


    fun getClientFrom(): String {
        return "android"
    }

    fun getAppName(): String {
        return "GOGOMARKET"
    }
}
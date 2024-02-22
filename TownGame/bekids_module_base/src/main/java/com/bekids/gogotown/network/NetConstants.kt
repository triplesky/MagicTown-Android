package com.bekids.gogotown.network

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:44 下午
 * 用途
 ***************************************

 */
object NetConstants {
    const val HTTP_REQUEST_TIMEOUT: Long = 5000L //

    const val SSOUID = "X-SSO-UID"
//    const val SSORSRIP = "X-SSO-RSR-IP"
    const val UserAgent = "User-Agent"
    const val SSOSIGN = "X-SSO-SIGN"


    /**
     * 	应用id
     */
    const val UID = "uid"

    /**
     * 	应用id
     */
    const val APPID = "appId"

    /**
     * 鉴权
     */
    const val TOKEN = "utoken"

    /**
     * 设备号
     */
    const val DEVICE_ID = "deviceid"

    /**
     * app设备号
     */
    const val APP_DEVICE_ID = "appDeviceId"

    /**
     * 设备类型
     */
    const val DEVICE = "device"

    /**
     * APP版本
     */
    const val APPVERSION = "appVersion"

    /**
     * 下载渠道

     */
    const val CHANNELID = "channelId"


    const val HeaderContentTypeLong = "Content-Type:application/json; charset=UTF-8"//"Content-Type:application/json; charset=UTF-8"


    /**
     * 使用缓存
     */
    const val USE_CACHE = "useCache:true"

    /**
     * 验签的时间戳
     */
    const val KsTimeStamp = "IH-Timestamp"


    /**
     * 网络添加cache的key
     */
    const val NETWORK_USER_CACHE_KEY = "NETWORK_USER_CACHE_KEY"
}
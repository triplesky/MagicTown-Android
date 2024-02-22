package com.bekids.gogotown.network.delegate

import android.app.Application
import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.network.HttpConfigHelper
import com.bekids.gogotown.network.NetConstants
import com.bekids.gogotown.router.RouterHelper
import com.google.gson.Gson
import com.ihuman.library.accountsdk.AccountHelp


import okhttp3.OkHttpClient
import okhttp3.internal.and
import java.io.File
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.HashMap

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:43 下午
 * 用途
 ***************************************

 */
class NetComponentConfigImpl : NetComponent.NetConfig {

    override fun isNeedSSL(): Boolean {
        return true
    }

    /**
     * 是否加载证书
     * @return Boolean
     */
    override fun openSignParam(): Boolean {
        return true
    }

    override fun getPemFile(): File {
        //如果asset证书没有效则使用从网络下载的
        return File("")
    }

    /**
     * requestId key and value
     * @return Pair<String, String>
     */
    override fun getQueryString(): Map<String, String> {
        return HashMap<String, String>().apply {
            put("_rid", "${System.currentTimeMillis()}")
        }
    }

    /**
     * 是否开启网络日志
     * @return Boolean
     */
    override fun isOpenLogging(): Boolean {
        return HttpConfigHelper.isOpenLogging()
    }

    /**
     * 验签 key
     * @return String?
     */
    override fun getSignKey(): String? {
        return ""
    }

    override fun customInterceptor(builder: OkHttpClient.Builder) {

    }

    override fun baseUrl(): String {
        return HttpConfigHelper.baseUrl()
    }

    override fun getAssetsPems(): MutableList<InputStream> {
        val certificates: MutableList<InputStream> = ArrayList()
        try {
            val enccharles = application().assets.open("enc-charles-ssl-proxying-certificate.pem")
            certificates.add(enccharles)
            val enccharles1 = application().assets.open(".pem")
            certificates.add(enccharles1)
        } catch (e: Exception) {
        }

        return certificates
    }

    override fun getHeaderMap(uri: String): Map<String, String> {
//        val token = (RouterHelper.buildUserInfoProvider() as? UserInfoProvider)?.getAccountInfo()
//        return HashMap<String, String>().apply {
//            (RouterHelper.buildUserInfoProvider() as? UserInfoProvider)?.getAccountInfo()?.let {
//                put(
//                    NetConstants.UID,
//                    it.uid
//                )
//            }
//            put(NetConstants.DEVICE_ID, AccountHelp.getInstance().deviceId)
//            put(NetConstants.TOKEN,  "")
//            token?.let {
//                it.utoken?.let {
//                    put(NetConstants.TOKEN,  token.utoken.utoken)
//                }
//            }
//        }
        return HashMap<String, String>()

    }

    override fun application(): Application {
        return BaseApplication.application
    }
}
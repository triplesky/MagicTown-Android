package com.bekids.gogotown.network.delegate

import android.app.Application
import androidx.annotation.UiThread
import okhttp3.OkHttpClient
import java.io.File
import java.io.InputStream

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:42 下午
 * 用途
 ***************************************

 */
object NetComponent {
    /**
     * 网络层主要配置
     */
    private var config: NetConfig? = null

    /**
     * token相关的代理
     */
    var tokenCallback: TokenCallback? = null

    private var isInit: Boolean = false

    /**
     * 代理初始化
     * @param config NetConfig
     * @param tokenCallback TokenCallback
     */
    fun init(config: NetConfig, tokenCallback: TokenCallback) {
        if (isInit) {
            return
        }
        NetComponent.config = config
        NetComponent.tokenCallback = tokenCallback
        isInit = true
    }

    /**
     * 获取headers
     * @return Map<String, String>
     */
    internal fun getHeader(uri: String): Map<String, String> {
        return checkConfig().getHeaderMap(uri)
    }

    /**
     * 是否打开证书验证
     */
    internal val isNeedSSL: Boolean by lazy { checkConfig().isNeedSSL() }

    /**
     * 是否开启验签
     */
    internal val openSignParam: Boolean by lazy { checkConfig().openSignParam() }

    /**
     * 网络库设置的baseurl
     */
    internal val baseUrl: String by lazy { checkConfig().baseUrl() }

    /**
     * 网络库所需context
     */
    val application: Application by lazy { checkConfig().application() }

    /**
     * 是否开启日志
     */
    internal val isOpenLogging: Boolean by lazy { checkConfig().isOpenLogging() }

    /**
     * apk内预制证书，也是需要加密过的
     * @return MutableList<InputStream>
     */
    fun getAssetsPems(): MutableList<InputStream> {
        return checkConfig().getAssetsPems()
    }

    /**
     * 从网络下载的证书目录
     * @return File
     */
    fun getPemFile(): File {
        return checkConfig().getPemFile()
    }

    /**
     * 获取requestId的key和value
     * @return Pair<String, String>
     */
    fun getQueryString(): Map<String, String>{
        return checkConfig().getQueryString()
    }

    /**
     * 获取验签key 如果app 没有key 返回null
     * @return
     */
    fun getSignKey(): String? {
        return checkConfig().getSignKey()
    }

    interface NetConfig {
        /**
         * 网络库所需的baseUrl
         * @return String
         */
        fun baseUrl(): String

        /**
         * 是否加载证书
         * @return Boolean
         */
        fun isNeedSSL(): Boolean

        /**
         * 是否加载证书
         * @return Boolean
         */
        fun openSignParam(): Boolean

        /**
         * 网络库加载的headers
         * @return Map<String, String>
         */
        fun getHeaderMap(uri: String): Map<String, String>

        /**
         * 网络库所需application
         * @return Application
         */
        fun application(): Application

        /**
         * apk内置证书
         * @return MutableList<InputStream>
         */
        fun getAssetsPems(): MutableList<InputStream>

        /**
         * the file which pem download from net.
         * @return File
         */
        fun getPemFile(): File

        /**
         * requestId key and value
         * @return Pair<String, String>
         */
        fun getQueryString(): Map<String, String>

        /**
         * 是否开启网络日志
         * @return Boolean
         */
        fun isOpenLogging(): Boolean

        /**
         * 验签 key
         * @return String?
         */
        fun getSignKey():String?

        /**
         * 添加自定义的拦截器
         * @param builder Builder
         */
        fun customInterceptor(builder: OkHttpClient.Builder)
    }


    interface TokenCallback {

        /**
         * 刷新本地token
         * @return needRetry ,token过期后 请求token之后 是否需要重新刷新当前接口
         * refreshToken获取成功则需要重刷
         *
         */
        suspend fun refreshToken(): Boolean

        /**
         * showToast 提示
         * @param message 提示信息
         */
        @UiThread
        fun showToast(message: String?)

        /**
         * 业务统一暴露处理
         * @param code Int
         * @param message String
         */
        @UiThread
        fun handlerEvent(code: Int, message: String?)

    }

    /**
     * check config is init
     * @return NetConfig
     */
    private fun checkConfig(): NetConfig {
        if (config == null) {
            throw RuntimeException("请先调用 NetComponent.init(config: NetConfig, tokenCallback: TokenCallback) 初始化 网络组件")
        }
        return config!!
    }

    /**
     * 添加自定义的拦截器
     * @param builder Builder
     */
    fun customInterceptor(builder: OkHttpClient.Builder) {
        checkConfig().customInterceptor(builder)
    }
}
package com.bekids.gogotown.network

import com.bekids.gogotown.network.NetConstants.HTTP_REQUEST_TIMEOUT
import com.bekids.gogotown.network.delegate.NetComponent
import com.bekids.gogotown.network.interceptors.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:35 下午
 * 用途
 ***************************************

 */
object IHumanRetrofitClient : BaseRetrofitClient() {

    fun excuter(): ExecutorService? = Executors.newSingleThreadExecutor()


    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder.connectTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(HeaderInterceptor())
            .apply {
                if (isOpenLogging()) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
    }

    override fun baseUrl(): String {
        return NetComponent.baseUrl
    }

    /**
     * 是否开启日志
     * @return Boolean
     */
    fun isOpenLogging(): Boolean {
        return NetComponent.isOpenLogging
    }
}
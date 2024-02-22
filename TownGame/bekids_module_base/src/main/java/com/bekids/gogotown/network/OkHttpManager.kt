package com.bekids.gogotown.network

import android.text.TextUtils
import com.bekids.gogotown.network.delegate.NetComponent
import com.bekids.gogotown.network.interceptors.HeaderInterceptor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 ***************************************
 * 项目名称: bekids Town
 * @Author ll
 * 创建时间: 2022/10/18    9:55 上午
 * 用途
 ***************************************

 */
class OkHttpManager private constructor(){

    companion object {
        val instance: OkHttpManager
            get() = OkHttpHolder.instance
    }

    private object OkHttpHolder {
        val instance = OkHttpManager()
    }

    private val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(NetConstants.HTTP_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(NetConstants.HTTP_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(NetConstants.HTTP_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
        .addInterceptor(HeaderInterceptor())
        .apply {
            if (NetComponent.isOpenLogging) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }


    fun initRequest(url: String, requestBody: RequestBody, headers: Map<String, String>?, callback: Callback,method:String): Call {
        val requestBuilder = Request.Builder()
            .url(url)
            .method(if (TextUtils.isEmpty(method)) "POST" else method, requestBody)
        headers?.let {
            val headerBuilder = Headers.Builder()

            for (key in headers.keys) {
                headerBuilder.add(key, headers.getValue(key))
            }
            requestBuilder.headers(headerBuilder.build())
        }

        val call = builder.build().newCall(requestBuilder.build())
        call.enqueue(callback)
        return call
    }

    fun initRequest4Get(url: String, headers: Map<String, String>?, callback: Callback,method:String): Call {
        val requestBuilder = Request.Builder()
            .url(url)
            .get()
        headers?.let {
            val headerBuilder = Headers.Builder()

            for (key in headers.keys) {
                headerBuilder.add(key, headers.getValue(key))
            }
            requestBuilder.headers(headerBuilder.build())
        }

        val call = builder.build().newCall(requestBuilder.build())
        call.enqueue(callback)
        return call
    }
}
package com.bekids.gogotown.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    11:46 上午
 * 用途
 ***************************************

 */
abstract class BaseRetrofitClient {
    private val client: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        handleBuilder(builder)
        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl())
            .build()
    }

    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)
    protected abstract fun baseUrl(): String

    fun <S> getService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    fun getOkHttpClient():OkHttpClient{
        return client
    }
}
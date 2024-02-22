package com.bekids.gogotown.network.interceptors

import com.bekids.gogotown.network.delegate.NetComponent
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/23    3:46 下午
 * 用途
 ***************************************

 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = headerJoinin(chain.request().newBuilder(), chain.request()).method(chain.request().method, chain.request().body).build()
        var proceed = chain.proceed(request)
        return proceed
    }

    /**
     * 添加公共请求头信息
     * 请求头来源 [NetComponent.getHeader]
     * @param builder Builder
     * @param request Request
     * @return Request.Builder
     */
    private fun headerJoinin(builder: Request.Builder, request: Request): Request.Builder {
        val path = request.url.toUrl().path


//        val buffer = Buffer()
//        request.body?.writeTo(buffer)
//        var charset: Charset = Charset.forName("UTF-8")
//        var content: MediaType? = request.body?.contentType()
//        if (content != null) {
//            charset = content.charset(charset)!!
//        }
//        val json: String = buffer.readString(charset)
//        builder.header("sign", JsonParser.parseString(json).asJsonObject.get("sign").toString())


//        val headerMap = NetComponent.getHeader(path).entries
//        headerMap.forEach { entry ->
//            builder.header(entry.key, entry.value)
//        }

        //如果contenttype 为空则默认为json的
//        val contentType = request.header("Content-Type")
//        if (contentType == null) {
//            builder.header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
//        }

        return builder
    }
}
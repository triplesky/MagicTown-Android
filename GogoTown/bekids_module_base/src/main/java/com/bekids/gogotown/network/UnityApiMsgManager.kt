package com.bekids.gogotown.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bekids.gogotown.network.delegate.NetComponent
import com.bekids.gogotown.network.interceptors.HeaderInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 ***************************************
 * 项目名称: bekids Town
 * @Author ll
 * 创建时间: 2022/10/18    6:05 下午
 * 用途
 ***************************************

 */
object UnityApiMsgManager : CoroutineViewModel() {
    private var newCall: Call? = null
    private lateinit var mOkHttpClient: OkHttpClient


    fun post_api(
        url: String,
        requestBody: RequestBody,
        headers: Map<String, String>?,
        result : MutableLiveData<String>
    ){
        mOkHttpClient = OkHttpClient.Builder()
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
            .build()


        call(url, headers, requestBody, result)

    }

    private fun call(
        url: String,
        headers: Map<String, String>?,
        requestBody: RequestBody,
        result: MutableLiveData<String>
    ) {
        launch(Dispatchers.IO) {
            headers?.let {
                val headerBuilder = Headers.Builder()

                for (key in headers.keys) {
                    headerBuilder.add(key, headers.getValue(key))
                }
                val request: Request =
                    Request.Builder().url(url).headers(headerBuilder.build()).post(requestBody)
                        .build()
                newCall = mOkHttpClient.newCall(request)
                newCall?.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        launch(Dispatchers.Main) {
                            val jsonObject = JSONObject()
                            try {
                                jsonObject.put("code", -1)
                                jsonObject.put("message", e.message)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            result.value = jsonObject.toString()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val obj = JSONObject(response.body!!.string())
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("type", "obj")
                            jsonObject.put("value", obj)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        launch(Dispatchers.Main) {
                            result.postValue(jsonObject.toString())
                        }
                    }

                })
            }
        }
    }
}
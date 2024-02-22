package com.bekids.gogotown.unity.bridge.strategy

import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.bekids.gogotown.BuildConfig
import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.base.utils.IHEventBus
import com.bekids.gogotown.bean.LoginInfoHolder
import com.bekids.gogotown.eventbus.BusMsg
import com.bekids.gogotown.network.HttpConfigHelper
import com.bekids.gogotown.network.OkHttpManager
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod
import com.bekids.gogotown.unity.bridge.bean.MessageConstants
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy
import com.bekids.gogotown.JniString
import com.ihuman.library.accountsdk.AccountHelp
import com.ihuman.library.accountsdk.IHNetworkHelp
import com.ihuman.sdk.lib.model.FailMessage
import com.ihuman.sdk.lib.network.api.IHNetFetch
import com.ihuman.sdk.lib.network.callback.IHNetworkFetchCallback
import com.ihuman.sdk.lib.network.core.HttpSignTools
import com.ihuman.sdk.lib.utils.IHUtilHelper
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/9/13    2:28 下午
 * 用途
 ***************************************

 */
@RegisterUnityMethod(
    MessageConstants.MESSAGE_GET_API,
    MessageConstants.MESSAGE_POST_API,
    MessageConstants.MESSAGE_GOGO_KEY
)
class ApiStrategyKt : BaseAbstractStrategy() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun process(method: String?, jsonArray: JSONArray?, blockId: String?): String {
        var result =""
        when (method) {
            MessageConstants.MESSAGE_POST_API ->{
                jsonArray?.let {
                    if (it.length() > 1) {
                        post_api(it[0].toString(), it[1] as JSONObject, blockId)
                    } else {
                        post_api(it[0].toString(), null, blockId)
                    }
                }

            }
            MessageConstants.MESSAGE_GET_API ->{
                Log.i("lldebug get", jsonArray.toString())
                get_api(jsonArray!![0].toString(), jsonArray!![1] as JSONObject,blockId)
            }
            MessageConstants.MESSAGE_GOGO_KEY -> {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("type", "s")
                    jsonObject.put("value", JniString.stringForJni())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                result = jsonObject.toString()
            }
            else -> {}
        }
        return result
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun post_api(url: String, param: JSONObject?, blockId: String?):String{
        var headers: MutableMap<String, String>? = null
        var map = HashMap<String, String>()
        var time = (System.currentTimeMillis()/1000).toString()
        map.run {
            put("app_version", BuildConfig.VERSION_NAME)
            put("device_type", if(IHUtilHelper.getInstance().isPad()) "pad" else "phone")
            put("deviceid", AccountHelp.getInstance().deviceId)
            put("platform", "android")
            put("timestamp", time)
            LoginInfoHolder.getLoginedUser()?.uid?.let {
                put("uid", LoginInfoHolder.getLoginedUserId().toString())
                LoginInfoHolder.getLoginedUser()?.utoken?.utoken?.let { put("utoken", it) }
            }
            param?.let {
                val iterator = param.keys()
                while (iterator.hasNext()) {
                    var key = iterator.next() as String
                    var value = param.getString(key)
                    put(key, value)
                }
            }
        }

        var list = mutableListOf<String>()
        var treeMap = TreeMap(map)
        treeMap.forEach {
            list.add(it.key + "="+it.value)
        }
        var str = list.joinToString("&")

        map.put("sign", HttpSignTools.buildAppParamSign( str))
        var request_url = "${HttpConfigHelper.baseUrl()}/gogo_town$url"
        var requestBody = initRequestBody(map)
        headers?.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
        var call: Call = OkHttpManager.instance.initRequest(request_url, requestBody,headers, object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                callUnity(
                    MessageConstants.MESSAGE_POST_API,
                    generateNormalFailedJson(-1, e.message),
                    blockId,
                    MessageConstants.MESSAGE_POST_API
                )
            }

            override fun onResponse(call: Call, res: Response) {
                var response = res
                if (response.code == 200) {
                    val obj = JSONObject(response.body!!.string())
                    Log.e("lldebug data", obj.toString())
                    println("lldebug:$obj")
                    if (obj.get("code") == 0) {
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("data", obj)
                            jsonObject.put("code", 0)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        callUnity(
                            MessageConstants.MESSAGE_POST_API,
                            generateNormalJson(jsonObject),
                            blockId,
                            MessageConstants.MESSAGE_POST_API
                        )
                    } else {
                        if (obj.get("code") == 252) {
                            LoginInfoHolder.clearUserInfo()
                            AccountHelp.getInstance().logoutWithBlock()
                            if (BaseApplication.token_invalid == 0) {
                                BaseApplication.token_invalid = 1
                                IHEventBus.instance?.post(BusMsg(BusMsg.SIGN_OUT_COMPLETE,true))
                                IHEventBus.instance?.post(BusMsg(BusMsg.NOTI_USER_RIGHTS_UPDATE, null))
                            }
                        } else{
                            BaseApplication.token_invalid = 0
                        }
                        callUnity(
                            MessageConstants.MESSAGE_POST_API,
                            generateNormalFailedJson(-1, response.message),
                            blockId,
                            MessageConstants.MESSAGE_POST_API
                        )
                    }



                } else {
                    callUnity(
                        MessageConstants.MESSAGE_POST_API,
                        generateNormalFailedJson(-1, response.message),
                        blockId,
                        MessageConstants.MESSAGE_POST_API
                    )

                }

            }

        }, "POST")
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun get_api(url: String, param: JSONObject, blockId: String?):String{
        var headers: MutableMap<String, String>? = null
        var map = HashMap<String, String>()
        var time = (System.currentTimeMillis()/1000).toString()
        map.run {
            put("app_version", BuildConfig.VERSION_NAME)
            put("device_type", if(IHUtilHelper.getInstance().isPad()) "pad" else "phone")
            put("deviceid", AccountHelp.getInstance().deviceId)
            put("platform", "android")
            put("timestamp", time)
            LoginInfoHolder.getLoginedUser()?.uid?.let {
                put("uid", LoginInfoHolder.getLoginedUserId().toString())
                LoginInfoHolder.getLoginedUser()?.utoken?.utoken?.let { put("utoken", it) }
            }
            param?.let {

//        if(MMKVUtils.getDefaultInstance().getInt("environment_type")==3){
//            return;
//        }
                val iterator = param.keys()
                while (iterator.hasNext()) {
                    var key = iterator.next() as String
                    var value = param.getString(key)
                    put(key, value)
                }
            }
        }

        var list = mutableListOf<String>()
        var treeMap = TreeMap(map)
        treeMap.forEach {
            list.add(it.key + "="+it.value)
        }
        var str = list.joinToString("&")


        var request_url = "${HttpConfigHelper.baseUrl()}/bekids_puzzle${url}?${str}&sign=${HttpSignTools.buildAppParamSign( str)}"
        headers?.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
        var call: Call = OkHttpManager.instance.initRequest4Get(request_url,headers, object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                callUnity(
                    MessageConstants.MESSAGE_POST_API,
                    generateNormalFailedJson(-1, e.message),
                    blockId,
                    MessageConstants.MESSAGE_POST_API
                )
            }

            override fun onResponse(call: Call, res: Response) {
                var response = res
                if (response.code == 200) {
                    val obj = JSONObject(response.body!!.string())
                    if (obj.get("code") == 0) {
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("data", obj)
                            jsonObject.put("code", 0)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        callUnity(
                            MessageConstants.MESSAGE_POST_API,
                            generateNormalJson(jsonObject),
                            blockId,
                            MessageConstants.MESSAGE_POST_API
                        )
                    } else {
                        if (obj.get("code") == 252) {
                            LoginInfoHolder.clearUserInfo()
                            AccountHelp.getInstance().logoutWithBlock()
                            if (BaseApplication.token_invalid == 0) {
                                BaseApplication.token_invalid = 1
                                IHEventBus.instance?.post(BusMsg(BusMsg.SIGN_OUT_COMPLETE,true))
                                IHEventBus.instance?.post(BusMsg(BusMsg.NOTI_USER_RIGHTS_UPDATE, null))
                            }
                        } else{
                            BaseApplication.token_invalid = 0
                        }
                        callUnity(
                            MessageConstants.MESSAGE_POST_API,
                            generateNormalFailedJson(-1, response.message),
                            blockId,
                            MessageConstants.MESSAGE_POST_API
                        )
                    }



                } else {
                    callUnity(
                        MessageConstants.MESSAGE_POST_API,
                        generateNormalFailedJson(-1, response.message),
                        blockId,
                        MessageConstants.MESSAGE_POST_API
                    )

                }

            }

        }, "GET")
        return ""
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRequestBody(map : HashMap<String, String>) : RequestBody{
        val requestBody : RequestBody
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

        map.forEach {
            builder.addFormDataPart(it.key, it.value)
        }
        requestBody = builder.build()
        return requestBody
    }

    private fun fetchApiWithUrl(url: String, signParam: String, type: String, blockId: String?): String? {
        var type = type
        var map = HashMap<String, String>()
        var time = (System.currentTimeMillis()/1000).toString()
        map.run {
            put("app_version", BuildConfig.VERSION_NAME)
            put("device_type", if(IHUtilHelper.getInstance().isPad()) "pad" else "phone")
            put("deviceid", AccountHelp.getInstance().deviceId)
            put("platform", "android")
            put("timestamp", time)
            put("uid", LoginInfoHolder.getLoginedUserId().toString())
            LoginInfoHolder.getLoginedUser()?.utoken?.utoken?.let { put("utoken", it) }
        }

        var list = mutableListOf<String>()
        var treeMap = TreeMap(map)
        treeMap.forEach {
            list.add(it.key + "="+it.value)
        }
        var str = list.joinToString("&")

        map.put("sign", HttpSignTools.buildAppParamSign( str))

        IHNetworkHelp.getInstance()
            .fetchAPIWithAllSign(
                url,
                map,
                if (type == "post") IHNetFetch.POST else IHNetFetch.GET,
                object : IHNetworkFetchCallback {
                    override fun onFailure(failmsg: FailMessage) {
                        fetchApiFailed(failmsg.code, failmsg.message, url, blockId)
                    }

                    override fun onSuccess(result: String) {
                        fetchApiSuccess(result, url, blockId)
                    }
                })
        return ""
    }

    private fun fetchApiSuccess(res: String, url: String, blockId: String?) {
        try {
            Log.d(
                "ApiStrategy", """
     ====fetchApiWithUrl====
     url:$url
     result:$res
     """.trimIndent()
            )
            val callback_obj = JSONObject()
            val `object` = JSONObject(res)
            callback_obj.putOpt("data", `object`)
            callback_obj.put("code", 0)
            callback_obj.put("url", url)
            callUnity(
                MessageConstants.MESSAGE_FETCH_API_WITH_URL,
                generateNormalJson(callback_obj),
                blockId,
                MessageConstants.MESSAGE_FETCH_API_WITH_URL
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun fetchApiFailed(code: Int, msg: String, url: String, blockId: String?) {
        val failJsonObject = JSONObject()
        try {
            failJsonObject.put("code", code)
            failJsonObject.put("message", msg)
            failJsonObject.put("url", url)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        callUnity(
            MessageConstants.MESSAGE_FETCH_API_WITH_URL,
            generateNormalJson(failJsonObject),
            blockId,
            MessageConstants.MESSAGE_FETCH_API_WITH_URL
        )
    }

    private fun parseUnKnownJson(hashMap: HashMap<String, String>, json: String) {
        if (!TextUtils.isEmpty(json)) {
            try {
                val jsonObject = JSONObject(json)
                val objs = jsonObject.keys()
                var key: String
                while (objs.hasNext()) {
                    key = objs.next()
                    val value = jsonObject.optString(key)
                    hashMap[key] = value
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }


}
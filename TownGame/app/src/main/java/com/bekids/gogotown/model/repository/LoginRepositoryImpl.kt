package com.bekids.gogotown.model.repository

import com.bekids.gogotown.BuildConfig
import com.bekids.gogotown.bean.UserInfo
import com.bekids.gogotown.bean.LoginInfoHolder
import com.bekids.gogotown.bean.UserInfoX
import com.bekids.gogotown.model.api.NetServices
import com.bekids.gogotown.model.bean.OrderBean
import com.bekids.gogotown.model.bean.ProductListBean
import com.bekids.gogotown.network.IHumanBaseRepository
import com.bekids.gogotown.network.bean.IHumanResult
import com.ihuman.library.accountsdk.AccountHelp
import com.ihuman.sdk.lib.network.core.HttpSignTools
import com.ihuman.sdk.lib.thirdapi.model.ResultMessage
import com.ihuman.sdk.lib.utils.IHUtilHelper
import java.util.*
import kotlin.collections.HashMap

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/17    5:26 下午
 * 用途
 ***************************************

 */
class LoginRepositoryImpl : LoginRepository, IHumanBaseRepository() {

    override suspend fun login(result: ResultMessage): IHumanResult<UserInfo> {
        var map = HashMap<String, String>()
        var time = (System.currentTimeMillis()/1000).toString()
        map.run {
            put("appid", BuildConfig.APP_ID.toString())
            put("app_version", BuildConfig.VERSION_NAME)
            put("device_type", if(IHUtilHelper.getInstance().isPad()) "pad" else "phone")
            put("deviceid", AccountHelp.getInstance().deviceId)
            put("platform", "android")
            put("ticket", result.ticket)
            put("timestamp", time)
            put("uid", result.userid)
        }

        var list = mutableListOf<String>()
        var treeMap = TreeMap(map)
        treeMap.forEach {
            list.add(it.key + "="+it.value)
        }
        var str = list.joinToString("&")

        map.put("sign", HttpSignTools.buildAppParamSign( str))


        return safeApiCall(call = {executeResponse(getService(NetServices::class.java).login(
            map
        ))})
    }




    override suspend fun logOut(): IHumanResult<Boolean> {
        var map = HashMap<String, String>()
        var time = (System.currentTimeMillis()/1000).toString()
        map.run {
            put("appid", BuildConfig.APP_ID.toString())
            put("app_version", BuildConfig.VERSION_NAME)
            put("device_type", if(IHUtilHelper.getInstance().isPad()) "pad" else "phone")
            put("deviceid", AccountHelp.getInstance().deviceId)
            put("platform", "android")
            put("timestamp", time)
            LoginInfoHolder.getLoginedUser()?.uid?.let {
                put("uid", LoginInfoHolder.getLoginedUserId().toString())
                LoginInfoHolder.getLoginedUser()?.utoken?.utoken?.let { put("utoken", it) }
            }
        }

        var list = mutableListOf<String>()
        var treeMap = TreeMap(map)
        treeMap.forEach {
            list.add(it.key + "="+it.value)
        }
        var str = list.joinToString("&")

        map.put("sign", HttpSignTools.buildAppParamSign( str))
        return safeApiCall(call = {executeResponse(getService(NetServices::class.java).logOut(map))})
    }

    override suspend fun getUserInfo(): IHumanResult<UserInfoX> {
        var map = HashMap<String, String>()
        var time = (System.currentTimeMillis()/1000).toString()
        map.run {
            put("appid", BuildConfig.APP_ID.toString())
            put("app_version", BuildConfig.VERSION_NAME)
            put("device_type", if(IHUtilHelper.getInstance().isPad()) "pad" else "phone")
            put("deviceid", AccountHelp.getInstance().deviceId)
            put("platform", "android")
            put("timestamp", time)
            LoginInfoHolder.getLoginedUser()?.uid?.let {
                put("uid", LoginInfoHolder.getLoginedUserId().toString())
                LoginInfoHolder.getLoginedUser()?.utoken?.utoken?.let { put("utoken", it) }
            }

        }

        var list = mutableListOf<String>()
        var treeMap = TreeMap(map)
        treeMap.forEach {
            list.add(it.key + "="+it.value)
        }
        var str = list.joinToString("&")

        map.put("sign", HttpSignTools.buildAppParamSign( str))
        return safeApiCall(call = {executeResponse(getService(NetServices::class.java).getUserInfo(map))})
    }

    override suspend fun getProductList(): IHumanResult<ProductListBean> {
        var map = HashMap<String, String>()
        var time = (System.currentTimeMillis()/1000).toString()
        map.run {
            put("appid", BuildConfig.APP_ID.toString())
            put("app_version", BuildConfig.VERSION_NAME)
            put("device_type", if(IHUtilHelper.getInstance().isPad()) "pad" else "phone")
            put("deviceid", AccountHelp.getInstance().deviceId)
            put("platform", "android")
            put("timestamp", time)
            LoginInfoHolder.getLoginedUser()?.uid?.let {
                put("uid", LoginInfoHolder.getLoginedUserId().toString())
                LoginInfoHolder.getLoginedUser()?.utoken?.utoken?.let { put("utoken", it) }
            }
        }

        var list = mutableListOf<String>()
        var treeMap = TreeMap(map)
        treeMap.forEach {
            list.add(it.key + "="+it.value)
        }
        var str = list.joinToString("&")

        map.put("sign", HttpSignTools.buildAppParamSign( str))
        return safeApiCall(call = {executeResponse(getService(NetServices::class.java).getProductList(map))})
    }

    override suspend fun createOrder(product_id : String): IHumanResult<OrderBean> {
        var map = HashMap<String, String>()
        var time = (System.currentTimeMillis()/1000).toString()
        map.run {
            put("appid", BuildConfig.APP_ID.toString())
            put("app_version", BuildConfig.VERSION_NAME)
            put("device_type", if(IHUtilHelper.getInstance().isPad()) "pad" else "phone")
            put("deviceid", AccountHelp.getInstance().deviceId)
            put("platform", "android")
            put("product_id", product_id)
            put("timestamp", time)
            LoginInfoHolder.getLoginedUser()?.uid?.let {
                put("uid", LoginInfoHolder.getLoginedUserId().toString())
                LoginInfoHolder.getLoginedUser()?.utoken?.utoken?.let { put("utoken", it) }
            }
        }

        var list = mutableListOf<String>()
        var treeMap = TreeMap(map)
        treeMap.forEach {
            list.add(it.key + "="+it.value)
        }
        var str = list.joinToString("&")

        map.put("sign", HttpSignTools.buildAppParamSign( str))
        return safeApiCall(call = {executeResponse(getService(NetServices::class.java).createOrder(map))})
    }

}
package com.bekids.gogotown.bean

import android.util.Log
import androidx.annotation.Nullable
import com.bekids.gogotown.base.utils.IHvPreference
import com.google.gson.Gson

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/7/27    2:41 下午
 * 用途
 ***************************************

 */
object UserInfoHolder {
    private const val USER_INFO = "user_info"
    var userJson: String by IHvPreference(USER_INFO, "")
    private var userInfo: UserInfoX? = null

    fun init() {

        //userJson 内容为空或者解析异常，userBean都置为null
        userInfo = try {
            Gson().fromJson(userJson, UserInfoX::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    @Nullable
    fun getUserInfo(): UserInfoX? {
        if (userInfo == null) {
            userInfo = Gson().fromJson(userJson, UserInfoX::class.java)
        }
        return userInfo
    }

    fun getUserRights():Rights? {
        if (userInfo == null) {
            userInfo = Gson().fromJson(userJson, UserInfoX::class.java)
        }
        return userInfo?.rights
    }

    fun saveUser(json: String, jsonBean: UserInfoX?=null) {
        try {
            var l = System.currentTimeMillis()
            userJson = json
            userInfo = jsonBean

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun clearUserInfo() {
        userJson = ""
        userInfo = null
    }

    fun log() {
        Log.i("lldebug", "userInfo: " + userJson)
    }
}
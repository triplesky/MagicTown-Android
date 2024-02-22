package com.bekids.gogotown.bean

import android.util.Log
import androidx.annotation.Nullable
import com.bekids.gogotown.base.utils.IHvPreference
import com.google.gson.Gson
import com.ihuman.sdk.lib.datasync.IHDataSyncComponent

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/23    11:16 上午
 * 用途
 ***************************************

 */
object LoginInfoHolder {

    private const val LOGIN_INFO = "login_info"
    var loginJson: String by IHvPreference(LOGIN_INFO, "")
    private var loginInfo: UserInfo? = null

    fun init() {

        //userJson 内容为空或者解析异常，userBean都置为null
        loginInfo = try {
            Gson().fromJson(loginJson, UserInfo::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    @Nullable
    fun getLoginedUser(): UserInfo? {
        if (loginInfo == null) {
            loginInfo = Gson().fromJson(loginJson, UserInfo::class.java)
        }
        return loginInfo
    }

    fun getLoginedUserId(): Long? {
        if (loginInfo == null) {
            loginInfo = Gson().fromJson(loginJson, UserInfo::class.java)
        }
        return loginInfo?.uid
    }

    fun isLogined(): Boolean {
        if (loginInfo == null) {
            loginInfo = Gson().fromJson(loginJson, UserInfo::class.java)
        }
        return loginInfo?.uid != 0L
    }

    fun getUserRights():Rights? {
        if (loginInfo == null) {
            loginInfo = Gson().fromJson(loginJson, UserInfo::class.java)
        }
        return loginInfo?.user_info?.rights
    }

    fun saveUser(json: String, jsonBean: UserInfo?=null) {
        try {
            var l = System.currentTimeMillis()
            loginJson = json
            loginInfo = jsonBean

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        //sdkLoginStateUpdate()
    }

    fun clearUserInfo() {
        loginJson = ""
        loginInfo = null
        //sdkLoginStateUpdate()

    }

    fun sdkLoginStateUpdate() {
        if (loginInfo != null) {
            loginInfo?.utoken?.let {
                IHDataSyncComponent.getInstance().setAppTokenAndRoleIdAndRoleToken(
                    it.utoken, null, null)
            }
        } else{
            IHDataSyncComponent.getInstance().setAppTokenAndRoleIdAndRoleToken(
                "", null, null)
        }
    }

    fun log() {
        Log.i("lldebug", "userInfo: " + loginJson)
    }
}
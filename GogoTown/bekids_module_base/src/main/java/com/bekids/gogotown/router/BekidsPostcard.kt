package com.bekids.gogotown.router

import android.content.Context
import android.os.Parcelable
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import java.io.Serializable

/**
 ***************************************
 * 项目名称: enlighten
 * @Author ll
 * 创建时间: 2022/7/21    3:40 下午
 * 用途
 ***************************************

 */
class BekidsPostcard(path: String) {
    private var postcard: Postcard = getThirdRouter().build(path)

    fun BekidsPostcard(){}


    fun getRouter(): ARouter {
        return getThirdRouter()
    }

    fun getThirdRouter(): ARouter {
        return ARouter.getInstance()
    }

    fun withString(key: String?, value: String?): BekidsPostcard? {
        postcard!!.withString(key, value)
        return this
    }

    fun withBoolean(key: String?, value: Boolean): BekidsPostcard? {
        postcard!!.withBoolean(key, value)
        return this
    }

    fun withShort(key: String?, value: Short): BekidsPostcard? {
        postcard!!.withShort(key, value)
        return this
    }

    fun withInt(key: String?, value: Int): BekidsPostcard? {
        postcard!!.withInt(key, value)
        return this
    }

    fun navigation(): Any? {
        //加上来源，下个页面可以用来统计使用
        return navigation(null)
//        return navigation(null);
    }

    fun navigation(context: Context?): Any? {
        return postcard!!.withString(
            RouterDelegate.getCurrentActivityStatisticKey(),
            RouterDelegate.getCurrentActivityStatisticName()
        ).navigation(context, null)
    }

    fun withSerializable(key: String?, value: Serializable?): BekidsPostcard? {
        postcard!!.withSerializable(key, value)
        return this
    }

    fun withLong(key: String?, value: Long): BekidsPostcard? {
        postcard!!.withLong(key, value)
        return this
    }

    fun withParcelable(key: String?, value: Parcelable?): BekidsPostcard? {
        postcard!!.withParcelable(key, value)
        return this
    }


    fun withFlags(flag: Int): BekidsPostcard? {
        postcard!!.withFlags(flag)
        return this
    }

    fun withTransition(enterAnim: Int, exitAnim: Int): BekidsPostcard? {
        postcard!!.withTransition(enterAnim, exitAnim)
        return this
    }

    fun navigation(mContext: Context?, requestCode: Int) {
        navigation(mContext, requestCode, null)
    }

    fun navigation(mContext: Context?, navigationCallback: NavigationCallback?) {
        postcard!!.navigation(mContext, navigationCallback)
    }

    fun navigation(mContext: Context?, requestCode: Int, callback: NavigationCallback?) {
//        withString(GlobalConstant.KEY_FROM_ACTIVITY, BridgeDelegate.getInstance().currentActivityStatisticName());
        getRouter().navigation(mContext, postcard, requestCode, callback)
    }
}
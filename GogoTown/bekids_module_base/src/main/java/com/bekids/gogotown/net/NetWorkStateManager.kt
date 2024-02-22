package com.bekids.gogotown.net

import android.content.Context
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bekids.gogotown.base.BaseApplication

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/18    5:57 下午
 * 用途
 ***************************************

 */
object NetWorkStateManager {
    /**
     * 网络状态监听LiveData
     */
    val mNetworkStateLiveData : UnPeekLiveData<NetState> = UnPeekLiveData<NetState>()

    val instance : NetWorkStateManager = NetWorkStateManager

    /**
     * 监听网络变化
     */
    @JvmStatic
    fun observer(owner : LifecycleOwner, observer : Observer<NetState>) {
        instance.mNetworkStateLiveData.observe(owner, observer)
    }

    /**
     * 全局监听网络变化
     */
    @JvmStatic
    fun observeForever(owner : LifecycleOwner, observer : Observer<NetState>) {
        instance.mNetworkStateLiveData.observeForever(observer)
    }

    /**
     * 全局监听网络变化
     */
    @JvmStatic
    fun removeObserver(observer : Observer<NetState>?) {
        observer?.let { instance.mNetworkStateLiveData.removeObserver(observer) }
    }

    /**
     * @return 当前网络是否可用 true 可用，false 不可用
     */
    @JvmStatic
    fun isNetworkAvailable() : Boolean {
        if (instance.mNetworkStateLiveData.value == null) {
            publishNetState(BaseApplication.context)
        }
        return instance.mNetworkStateLiveData.value?.isSuccess ?: false
    }

    /**
     * 获取当前的网络类型
     * return 网络类型 look->[NetType]
     */
    @JvmStatic
    fun getCurrentNetType() : @NetType Int {
        if (instance.mNetworkStateLiveData.value == null) {
            publishNetState(BaseApplication.context)
        }
        return instance.mNetworkStateLiveData.value?.netType ?: NetType.NET_TYPE_UNKNOW
    }

    /**
     * 发布网络状态
     * 协程方式发布，避免anr
     */
    @JvmStatic
    internal fun publishNetState(context : Context) {
        //    if (!NetWorkUtil.isNetworkAvailable(context)) {
        // NetWorkUtil.getCurrentNetType(context)
        //收到没有网络时判断之前的值是不是有网络，如果有网络才提示通知 ，防止重复通知
        //                instance.mNetworkStateLiveData.value?.let {
        //                    if (it.isSuccess) {
        //                       send(NetState(isSuccess = false, netType = NetWorkUtil.getCurrentNetType(context)))
        //                    }
        //                    return
        //                }
        //send(NetState(isSuccess = NetWorkUtil.isNetworkAvailable(context), netType = NetWorkUtil.getCurrentNetType(context)))
        //   } else {
        //收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
        //                instance.mNetworkStateLiveData.value?.let {
        //                    if (!it.isSuccess) {
        //                        //有网络了
        //                        send(NetState(isSuccess = true, netType = NetWorkUtil.getCurrentNetType(context)))
        //                    }
        //                    return
        //                }
        //    send(NetState(isSuccess = true, netType = NetWorkUtil.getCurrentNetType(context)))
        //   }
    }

    // 发布事件
    private fun send(newState : NetState) {
        // 判断状态是否有变更
        instance.mNetworkStateLiveData.value?.apply {
            // 如果新状态和liveData中状态不一致 发送通知，目的避免重复发送
            if (isSuccess == newState.isSuccess && netType == newState.netType) {
                return
            }
        }
        // 主线程直接setValue
        if (Thread.currentThread().id == Looper.getMainLooper().thread.id) {
            instance.mNetworkStateLiveData.value = newState
        } else {
            //异步 postValue
            instance.mNetworkStateLiveData.postValue(newState)
        }
    }
}
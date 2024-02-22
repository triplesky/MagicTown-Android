package com.bekids.gogotown.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.bekids.gogotown.base.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/18    6:03 下午
 * 用途
 ***************************************

 */
class NetStateReceiver : BroadcastReceiver() {
    init {
        // 初始化时获取一次网络的状态
        NetWorkStateManager.publishNetState(BaseApplication.context)
    }
    override fun onReceive(context : Context?, intent : Intent?) {
        // 网络变化
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            GlobalScope.launch(Dispatchers.IO) {
                NetWorkStateManager.publishNetState(context ?: BaseApplication.context)
            }
        }
    }
}
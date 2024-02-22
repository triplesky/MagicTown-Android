package com.bekids.gogotown.starttask

import com.aice.appstartfaster.task.AppStartTask
import com.bekids.gogotown.network.delegate.NetComponent
import com.bekids.gogotown.network.delegate.NetComponentConfigImpl
import com.bekids.gogotown.network.delegate.NetComponetTokenCallbackImpl

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/20    2:24 下午
 * 用途
 ***************************************

 */
class InitNetComponentTask : AppStartTask() {
    override fun run() {
        NetComponent.init(NetComponentConfigImpl(), NetComponetTokenCallbackImpl())
    }

    override fun isRunOnMainThread(): Boolean {
        return false
    }
}
package com.bekids.gogotown.starttask

import com.aice.appstartfaster.task.AppStartTask
import com.alibaba.android.arouter.launcher.ARouter
import com.bekids.gogotown.BuildConfig
import com.bekids.gogotown.MainApplication
import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.router.RouterDelegate
import com.bekids.gogotown.util.GlobalConstants

/**
 ***************************************
 * 项目名称: enlighten
 * @Author ll
 * 创建时间: 2022/7/21    3:48 下午
 * 用途
 ***************************************

 */
class ARouterTask : AppStartTask() {
    override fun run() {
        if (BuildConfig.DEBUG) { //日志开启
            ARouter.openLog()
            //调试模式开启，如果在install run模式下运行，则必须开启调试模式
            ARouter.openDebug()
        }
        ARouter.init(BaseApplication.instance)
        RouterDelegate.init(object :RouterDelegate.RouterConfig{
            override fun getCurrentActivityStatisticKey(): String? {
                return GlobalConstants.KEY_FROM_ACTIVITY
            }

            override fun getCurrentActivityStatisticName(): String? {
                return ""
            }
        })
    }



    override fun isRunOnMainThread(): Boolean {
       return false
    }

    override fun needWait(): Boolean {
        return true
    }
}
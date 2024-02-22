package com.bekids.gogotown

import android.content.Context
import android.os.Process
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDexApplication
import com.aice.appstartfaster.dispatcher.AppStartTaskDispatcher
import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.di.mainModule
import com.bekids.gogotown.ext.activityManager
import com.bekids.gogotown.ext.isMainProcess
import com.bekids.gogotown.network.HttpConfigHelper
import com.bekids.gogotown.starttask.ARouterTask
import com.bekids.gogotown.starttask.AccountTask
import com.bekids.gogotown.starttask.InitNetComponentTask
import com.ihuman.sdk.module.account.IHAccountUIComponent
import com.tencent.bugly.crashreport.CrashReport
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

/**
 ***************************************
 * 项目名称: gogopuzzle
 * @Author ll
 * 创建时间: 2022/7/22    2:31 下午
 * 用途
 ***************************************

 */
class MainApplication : BaseApplication() {

    companion object {
        lateinit var mainActivity: MainActivity
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        application = this
        initHttpConfig()
        if (isMainProcess()) {
            initStarter()
            CrashReport.initCrashReport(applicationContext, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG)
        }
    }

    private fun initStarter() {
        AppStartTaskDispatcher.create()
            .setShowLog(true)
            .addAppStartTask(ARouterTask())
            .addAppStartTask(AccountTask())
            .addAppStartTask(InitNetComponentTask())
            .start()
            .await()
    }

    fun getProcessName(context: Context?): String? {
        try {
            val runningApps = context?.activityManager?.runningAppProcesses ?: return null
            for (proInfo in runningApps) {
                if (proInfo.pid == Process.myPid()) {
                    if (proInfo.processName != null) {
                        return proInfo.processName
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun attachBaseContext(base: Context?) {
        Log.i("ldb",base?.packageName +", "+ getProcessName(base) )
        if (base?.packageName == getProcessName(base)) {
            val koin = GlobalContext.getOrNull()
            if (koin == null) {
                startKoin {
                    androidLogger()
                    androidContext(base?:this@MainApplication)
                }
                base?.let {
                    if (isMainProcess(it)) {
                        loadKoinModules(mainModule)
                    }
                }

            }
        }
        super.attachBaseContext(base)
    }
    fun isMainProcess(context : Context) : Boolean {
        return context.packageName == getProcessName(context)
    }

    private fun initHttpConfig() {
        HttpConfigHelper.initConfig(BuildConfig.APP_ENVIRONMENT)
    }

    override fun onLowMemory() {
        Log.i("lldebug app onLowMemory", "onLowMemory")
        super.onLowMemory()
    }

    // Trim Memory Unity

    override fun onTrimMemory(level: Int) {
        Log.i("lldebug app", "onTrimMemory")
        super.onTrimMemory(level)
    }
}
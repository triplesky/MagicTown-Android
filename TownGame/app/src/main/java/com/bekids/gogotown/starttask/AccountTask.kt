package com.bekids.gogotown.starttask

import androidx.annotation.NonNull
import com.aice.appstartfaster.task.AppStartTask
import com.bekids.gogotown.BuildConfig
import com.bekids.gogotown.MainApplication
import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.base.utils.MMKvUtils
import com.ihuman.library.accountsdk.AccountHelp
import com.ihuman.sdk.lib.attribution.IHAttributionComponent
import com.ihuman.sdk.lib.attribution.callback.IHAFAttributionCallback
import com.ihuman.sdk.lib.download.IHDownloadComponent
import com.ihuman.sdk.lib.environment.NS_ENUM
import com.ihuman.sdk.lib.thirdapi.channel.IHChannelType
import com.ihuman.sdk.lib.thirdapi.channel.IHRegisterType
import com.ihuman.sdk.lib.utils.ChannelUtils
import com.ihuman.sdk.module.account.IHAccountUIComponent

/**
 ***************************************
 * 项目名称: enlighten
 * @Author ll
 * 创建时间: 2022/7/21    3:54 下午
 * 用途
 ***************************************

 */
class AccountTask : AppStartTask() {
    override fun run() {
        var clear_device_info = false
        clear_device_info = MMKvUtils["clear_device_info", false] == true
        AccountHelp.Builder()
            .setAppId(BuildConfig.APP_ID.toString())//设置appId
            .setAppKey(BuildConfig.APP_KEY)//设置appKey
            .setAppName(BuildConfig.APP_NAME)//设置app
            .setPublicKey(BuildConfig.APP_PUBLIC_KEY)//设置PublicKey
            .allowShowLog(BuildConfig.DEBUG)//设置是否输出log
            .setEnviroment(if (BuildConfig.APP_ENVIRONMENT == 3) NS_ENUM.IHEnvironmentTypeProduction else NS_ENUM.IHEnvironmentTypeStaging)//设置staging 或者preduction的
            .setChannel(ChannelUtils.GOOGLE)//设置渠道
            .registerChannels(IHRegisterType.GOOGLE,"", BuildConfig.GOOGLE)
            //.registerChannels(IHRegisterType.FACEBOOK, BuildConfig.FB_ID, BuildConfig.FB_KEY)
            .setTestDevicesIdModel(if (BuildConfig.APP_ENVIRONMENT == 2) clear_device_info else false)//设置是否使用测试的deviceId
            .setLocalTimeStamp(true)//设置是否使用本地时间
            .build(BaseApplication.instance);

        IHAttributionComponent.getInstance()
            .enableAFAttribution(BaseApplication.instance, object : IHAFAttributionCallback {
                override fun onConversionData(@NonNull data: String) {

                }
            })
        IHAttributionComponent.getInstance().startAFAttribution()
        IHAttributionComponent.getInstance().enableFirebase(BaseApplication.instance, true)
        //IHAttributionComponent.getInstance().enableFacebook(BaseApplication.instance, true)

        IHAccountUIComponent.getInstance().initialize(BaseApplication.instance)
    }

    override fun getDependsTaskList(): MutableList<Class<out AppStartTask>> {
        return mutableListOf(ARouterTask::class.java)
    }

    override fun isRunOnMainThread(): Boolean {
        return true
    }
}
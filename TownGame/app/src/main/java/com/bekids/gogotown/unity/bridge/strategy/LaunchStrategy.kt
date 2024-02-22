//package com.bekids.gogotown.unity.bridge.strategy
//
//
//import android.content.Intent
//import com.bekids.gogotown.MainActivity
//import com.bekids.gogotown.MainApplication
//import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod
//import com.bekids.gogotown.unity.bridge.bean.MessageConstants
//import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy
//import com.ihuman.library.accountsdk.AccountHelp
//
//import com.ihuman.sdk.module.launch.IHLaunchComponent
//import com.ihuman.sdk.module.launch.callback.IHLaunchViewCallback
//import com.ihuman.sdk.module.update.IHCheckUpdateHelp
//import org.json.JSONArray
//
///**
// ***************************************
// * 项目名称: gogomarket
// * @Author ll
// * 创建时间: 2022/7/25    10:19 上午
// * 用途
// ***************************************
//
// */
//@RegisterUnityMethod(MessageConstants.MESSAGE_APP_SHOW_LAUNCH_VIEW_ACTION)
//class LaunchStrategy : BaseAbstractStrategy() {
//    override fun process(method: String?, jsonArray: JSONArray?, blockId: String?): String {
//        val result = ""
//        when (method) {
//            MessageConstants.MESSAGE_APP_SHOW_LAUNCH_VIEW_ACTION -> {
//                //callUnity(method, "", blockId, "")
//                showLaunch(method, jsonArray, blockId)
//            }
//        }
//        return result
//    }
//
//    private fun showLaunch(method: String, jsonArray: JSONArray?, blockId: String?) {
//
//        MainApplication.mainActivity.runOnUiThread(object :Runnable {
//            override fun run() {
//                IHLaunchComponent.getInstance()
//                    .showLaunchView(MainApplication.mainActivity, object : IHLaunchViewCallback {
//                        override fun onDismiss() {
//                            //IHCheckUpdateHelp.getInstance().showUpdateViewIfPossible()
//                            callUnity(method, "", blockId, "")
//
//                            AccountHelp.getInstance().getIhumanHandler().postDelayed(object :Runnable{
//                                override fun run() {
//                                    val intent = Intent(context, MainActivity::class.java)
//                                    context.startActivity(intent)
//                                }
//
//                            }, 1)
//
//                        }
//                    })
//            }
//
//        })
//
//
//    }
//}
package com.bekids.gogotown.unity.bridge.strategy

import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.bekids.gogotown.MainApplication
import com.bekids.gogotown.R
import com.bekids.gogotown.permission.PermissionsUtils
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod
import com.bekids.gogotown.unity.bridge.bean.MessageConstants
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import org.json.JSONArray
import org.json.JSONObject

/**
 ***************************************
 * 项目名称: bekids Town
 * @Author ll
 * 创建时间: 2023/1/3    6:18 下午
 * 用途
 ***************************************

 */
@RegisterUnityMethod(
     MessageConstants.MESSAGE_SHOW_SETTING_ALERT_VIEW
)
class SettingUtilStrategy : BaseAbstractStrategy() {
    override fun process(method: String?, jsonArray: JSONArray?, blockId: String?): String {
        var result =""
        when (method) {
            MessageConstants.MESSAGE_SHOW_SETTING_ALERT_VIEW ->{
                showSettingAlert(jsonArray!![0].toString(),jsonArray!![1].toString(),blockId, MessageConstants.MESSAGE_SHOW_SETTING_ALERT_VIEW)
            }
            else -> {}
        }
        return result
    }

    private fun showSettingAlert(t:String,content: String,  blockId: String?, method: String?) {
        MainApplication.mainActivity.runOnUiThread(object :Runnable{
            override fun run() {
                DialogPlus.newDialog(MainApplication.mainActivity)
                    .setMargin(0,0,0,0)
                    .setContentHolder(ViewHolder(R.layout.setting_per_dialog))
                    .setContentBackgroundResource(R.color.ui_color_transparent)
                    .setOverlayBackgroundResource(R.color.dialog_bg)
                    .setGravity(Gravity.CENTER)
                    .setExpanded(false)
                    .setCancelable(false)
                    .create().apply {
                        var btnLeft : TextView = findViewById(R.id.btn_cancel) as TextView
                        var btnRight : TextView = findViewById(R.id.btn_ok) as TextView

                        var version : TextView = findViewById(R.id.version_detail) as TextView
                        version.text = content
                       var title : TextView =  findViewById(R.id.tvCourseDialogDetainTip) as TextView
                        title.text = t
                        val json = JSONObject()
                        btnRight.setOnClickListener(object : View.OnClickListener{
                            override fun onClick(v: View?) {
                                PermissionsUtils.setPermission(MainApplication.mainActivity, null)
                                json.put("success", true)
                                callUnity(method, generateNormalJson(json), blockId, method)
                                dismiss()
                            }

                        })
                        btnLeft.setOnClickListener(object : View.OnClickListener{
                            override fun onClick(v: View?) {
                                json.put("success",false)
                                callUnity(method, generateNormalJson(json), blockId, method)
                                dismiss()
                            }

                        })
                        show()

                    }
            }

        })

    }
}
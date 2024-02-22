package com.bekids.gogotown.unity.bridge.strategy

import android.text.TextUtils
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ConsumeResponseListener
import com.bekids.gogotown.BuildConfig
import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.base.utils.IHEventBus.instance
import com.bekids.gogotown.base.utils.MMKvUtils
import com.bekids.gogotown.bean.LoginInfoHolder
import com.bekids.gogotown.bean.LoginInfoHolder.loginJson
import com.bekids.gogotown.bean.UserInfoHolder
import com.bekids.gogotown.eventbus.BusMsg
import com.bekids.gogotown.ext.toast
import com.bekids.gogotown.model.bean.Product
import com.bekids.gogotown.model.bean.ProductListHolder
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod
import com.bekids.gogotown.unity.bridge.bean.MessageConstants
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy
import com.google.gson.Gson
import com.ihuman.library.accountsdk.AccountHelp
import com.ihuman.library.accountsdk.google.billing.IHBillingClient
import com.ihuman.sdk.lib.account.model.IHAccountBean
import com.ihuman.sdk.lib.model.FailMessage
import com.ihuman.sdk.lib.pay.api.model.IHProductType
import com.ihuman.sdk.lib.pay.api.model.ProductType
import com.ihuman.sdk.lib.thirdapi.IHumanCallback
import com.ihuman.sdk.lib.thirdapi.model.ResultMessage
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/7/25    5:16 下午
 * 用途
 ***************************************

 */
@RegisterUnityMethod(
    MessageConstants.MESSAGE_ACCOUNT_LOGIN,
    MessageConstants.MESSAGE_ACCOUNT_LOGOUT,
    MessageConstants.MESSAGE_CURRENT_ACCOUNT,
    MessageConstants.MESSAGE_LAST_ACCOUNT,
    MessageConstants.MESSAGE_ACCOUNT_REFRESH_USER_INFO,
    MessageConstants.MESSAGE_ACCOUNT_SHOW_TOAST,
    MessageConstants.MESSAGE_ACCOUNT_GET_LOGIN_INFO,
    MessageConstants.MESSAGE_ACCOUNT_GET_USER_INFO,
    MessageConstants.MESSAGE_ACCOUNT_GET_USER_RIGHTS,
    MessageConstants.MESSAGE_ACCOUNT_NOTI_USER_INFO_UPDATE,
    MessageConstants.MESSAGE_ACCOUNT_HAS_USER_RIGHTS,
    MessageConstants.MESSAGE_PAY_PRODUCT_INFO_FOR_USER_RIGHTS_TYPE,
    MessageConstants.MESSAGE_USER_STATUS,
    MessageConstants.MESSAGE_ACCOUNT_GET_LOGIN_STATE,
    MessageConstants.MESSAGE_HAS_VISITOR_RIGHTS,
    MessageConstants.MESSAGE_HAS_ANY_VISITOR_RIGHTS ,
    MessageConstants.MESSAGE_CLEAR_IAP_CACHE,
    MessageConstants.MESSAGE_CLEAR_DEVICE_INFO
)
class AccountStrategy : BaseAbstractStrategy() {
    var currentMethod: String? = null
    var currentBlockId: String? = null
    var blockIdMap = HashMap<String?, String>()

    @Throws(JSONException::class, IndexOutOfBoundsException::class)
    override fun process(method: String?, jsonArray: JSONArray, blockId: String?): String? {
        instance?.let {
            if (!it.isRegistered(this)) {
                it.register(this)
            }
        }

        var result: String? = ""
        blockIdMap[method] = blockId!!
        when (method) {
            MessageConstants.MESSAGE_ACCOUNT_LOGIN -> {
                instance!!.post(BusMsg(BusMsg.SIGN_IN, null))
            }
            MessageConstants.MESSAGE_ACCOUNT_LOGOUT -> {
                instance!!.post(BusMsg(BusMsg.SIGN_OUT, null))
            }
            MessageConstants.MESSAGE_CURRENT_ACCOUNT ->{
                result = generateObjJson(JSONObject(Gson().toJson(AccountHelp.getInstance().currentAccount, IHAccountBean::class.java)))
            }

            MessageConstants.MESSAGE_LAST_ACCOUNT ->{
                result = generateObjJson(JSONObject(Gson().toJson(AccountHelp.getInstance().lastAccount, IHAccountBean::class.java)))
            }
            MessageConstants.MESSAGE_ACCOUNT_GET_LOGIN_INFO -> {
                result = if (loginJson.isNullOrEmpty()) {
                    ""
                } else {
                    var json = JSONObject(loginJson)
                    json.put("new_user", LoginInfoHolder.getLoginedUser()?.new_user == 1)
                    generateObjJson(json)
                }
            }
            MessageConstants.MESSAGE_ACCOUNT_GET_USER_INFO -> {
                result = if (loginJson.isNullOrEmpty()) {
                    ""
                } else {
                    generateObjJson(JSONObject(loginJson))
                }
            }
            MessageConstants.MESSAGE_ACCOUNT_GET_USER_RIGHTS -> {
                result =
//                    if (loginJson.isNullOrEmpty()) {
//                    var right = JSONObject()
//                    right.put("all_pack", false)
//                    right.put("vip", null)
//                    right.put("vip_expire_time", null)
//                    right.put("plus_vip", null)
//                    right.put("plus_vip_expire_time", null)
//                    generateObjJson(right)
//                } else {
                    if (UserInfoHolder.getUserRights() == null) {
                        var right = JSONObject()
//                        right.put("all_pack", false)
//                        right.put("vip", null)
//                        right.put("pack",null)
//                        right.put("vip_expire_time", null)
//                        right.put("plus_vip", null)
//                        right.put("plus_vip_expire_time", null)
                        generateObjJson(right)
                    } else {
                        UserInfoHolder.getUserRights()?.let {
                            var right = JSONObject()
                            right.put("all_pack", it.all_pack)
                            right.put("vip", it.vip)
                            right.put("pack",null)
                            right.put("pack",listCovert2arr(it.pack))
                            right.put("vip_expire_time", it.vip_expire_time)
                            right.put("plus_vip", it.plus_vip)
                            right.put("plus_vip_expire_time", it.plus_vip_expire_time)
                            generateObjJson(right)
                        }
                    }


//                }
            }

            MessageConstants.MESSAGE_ACCOUNT_SHOW_TOAST -> {
                if (!TextUtils.isEmpty(jsonArray.getString(0))) {
                    BaseApplication.instance.toast(jsonArray.getString(0))
                }
            }
            MessageConstants.MESSAGE_ACCOUNT_REFRESH_USER_INFO -> {
                instance!!.post(BusMsg(BusMsg.REFRESH_USER_INFO,null))

            }

            MessageConstants.MESSAGE_ACCOUNT_HAS_USER_RIGHTS -> {

            }
            MessageConstants.MESSAGE_PAY_PRODUCT_INFO_FOR_USER_RIGHTS_TYPE -> {
                var product = ProductListHolder.getProductInfo(jsonArray[0] as String)
                if (product != null) {
                    result = generateObjJson(JSONObject(Gson().toJson(product, Product::class.java)))
                } else {
                    var p = Product("",-1,"","-1","",-1,"", 0.0)
                    result = generateObjJson(JSONObject(Gson().toJson(p, Product::class.java)))
                }
            }

            MessageConstants.MESSAGE_USER_STATUS -> {
                var jsonObject = JSONObject()
                try {
                    if (MMKvUtils["user_status"+ BuildConfig.VERSION_CODE, -1] == -1) {
                        jsonObject.put("type", "obj")
                        jsonObject.put("value", 1)
                    } else{
                        jsonObject.put("type", "obj")
                        jsonObject.put("value", MMKvUtils["user_status"+ BuildConfig.VERSION_CODE, -1])
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                result = jsonObject.toString()
            }

            MessageConstants.MESSAGE_ACCOUNT_GET_LOGIN_STATE -> {
                var value = if (BaseApplication.token_invalid == 1) 2 else {
                    if (LoginInfoHolder.getLoginedUser() != null && LoginInfoHolder.getLoginedUser()?.uid != null) {
                        1
                    } else {
                        0
                    }
                }

                var o = JSONObject()
                o.put("type", "obj")
                o.put("value", value)
                result = generateNormalJson(o)
            }
            MessageConstants.MESSAGE_HAS_VISITOR_RIGHTS ->{
                result = generateBooleanJson(false)
            }
            MessageConstants.MESSAGE_HAS_ANY_VISITOR_RIGHTS -> {
                result = generateBooleanJson(false)
            }
            MessageConstants.MESSAGE_CLEAR_IAP_CACHE ->{
                consumePurchase()
            }
            MessageConstants.MESSAGE_CLEAR_DEVICE_INFO -> {
                MMKvUtils.put("clear_device_info", true)
            }
        }
        return result
    }

    fun consumePurchase() {
        IHBillingClient.getInstance().queryAllPurchaseAsync { responseCode, purchases ->
            if (responseCode == BillingClient.BillingResponseCode.OK) {
                val purchase = purchases?.find { IHProductType.getProductType(it.products[0]) == ProductType.UN_CONSUMABLE}
                if (purchase == null) {

                } else {
                    IHBillingClient.getInstance().consumePurchase(purchase.purchaseToken, ConsumeResponseListener { p0, p1 ->
                        if (p0.responseCode == BillingClient.BillingResponseCode.OK) {

                        } else {

                        }
                    })
                }
            }
        }
    }

    @Throws(JSONException::class)
    private fun listCovert2arr(list: List<String>): JSONArray? {
        val jsonArray = JSONArray()
        val tmpObj: JSONObject? = null
        if (!list.isNullOrEmpty()) {
            val count = list.size
            for (i in 0 until count) {
                jsonArray.put(list[i])
            }
        }
        return jsonArray
    }

    open fun pickImageForAvatar(method: String, blockId: String?): String {
//        pickImageBlockId  = blockId;
//        ImagePickerDialog imagePickerDialog = ImagePickerDialog.newInstance(method,blockId);
//        FragmentActivity activity = (FragmentActivity) getContext();
//        imagePickerDialog.setOnPickCompleteListener(this);
//        imagePickerDialog.show(activity.getSupportFragmentManager(),"ImagePicker");
        return "pickImageForAvatar"
    }

    open fun finishLoginWithToken(uToken: String): String? {
        return generateBooleanJson(AccountHelp.getInstance().finishedLoginWithUtoken(uToken))
    }

    open fun logoutWithBlock(): String? {
        return generateBooleanJson(AccountHelp.getInstance().logoutWithBlock())
    }

    open fun updateUserAvatar(
        method: String,
        path: String,
        quality: Int,
        blockId: String?
    ): String? {
        AccountHelp.getInstance()
            .updataAvatarImage(path, quality, object : IHumanCallback {
                override fun onSuccess(result: ResultMessage) {
                    val successJsonObject = JSONObject()
                    try {
                        successJsonObject.put("code", result.code)
                        successJsonObject.put("message", result.message)
                        successJsonObject.put("avatar_id", result.avatar)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    callUnity(
                        method,
                        generateNormalJson(successJsonObject),
                        blockId,
                        MessageConstants.MESSAGE_PICK_IMAGE_FOR_AVATAR
                    )
                }

                override fun onFailure(failMessage: FailMessage) {
                    val failJsonObject = JSONObject()
                    try {
                        failJsonObject.put("code", failMessage.code)
                        failJsonObject.put("message", failMessage.message)
                        failJsonObject.put("url", path)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    callUnity(
                        method,
                        generateNormalJson(failJsonObject),
                        blockId,
                        MessageConstants.MESSAGE_PICK_IMAGE_FOR_AVATAR
                    )
                }
            })
        return "updateUserAvatar"
    }

    open fun generatePickSuccessJson(url: String): String? {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("code", 0)
            jsonObject.put("file_path", url)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject.toString()
    }

    open fun generateFailedJson(): String? {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("code", 1)
            jsonObject.put("message", "图片选择失败")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject.toString()
    }


    @Subscribe
    fun onEvent(event: BusMsg<*>) {
        when (event.code) {
            BusMsg.SIGN_IN_COMPLETE -> {
                callUnity(
                    MessageConstants.MESSAGE_ACCOUNT_NOTI_LOGIN_UPDATE,
                    "",
                    "",
                    MessageConstants.MESSAGE_ACCOUNT_NOTI_LOGIN_UPDATE
                )
                val json = JSONObject()
                try {
                    json.put("code", 0)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                callUnity(
                    MessageConstants.MESSAGE_ACCOUNT_LOGIN,
                    generateObjJson(json),
                    blockIdMap[MessageConstants.MESSAGE_ACCOUNT_LOGIN],
                    MessageConstants.MESSAGE_ACCOUNT_LOGIN
                )

            }
            BusMsg.SIGN_OUT_COMPLETE -> {
                callUnity(
                    MessageConstants.MESSAGE_ACCOUNT_NOTI_LOGIN_UPDATE,
                    "",
                    "",
                    MessageConstants.MESSAGE_ACCOUNT_NOTI_LOGIN_UPDATE
                )
            }

            BusMsg.NOTI_USER_RIGHTS_UPDATE,
            BusMsg.GET_USER_INFO_COMPLETE -> {
                callUnity(
                    MessageConstants.MESSAGE_ACCOUNT_NOTI_USER_INFO_UPDATE,
                    "",
                    "",
                    MessageConstants.MESSAGE_ACCOUNT_NOTI_USER_INFO_UPDATE
                )
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("code", 0)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                callUnity(
                    MessageConstants.MESSAGE_ACCOUNT_REFRESH_USER_INFO,
                    generateObjJson(jsonObject),
                    blockIdMap[MessageConstants.MESSAGE_ACCOUNT_REFRESH_USER_INFO],
                    MessageConstants.MESSAGE_ACCOUNT_REFRESH_USER_INFO
                )

            }
            BusMsg.APP_EXIT ->{
                callUnity(
                    MessageConstants.MESSAGE_ANDROID_APPLICATION_EXIT_ACTION,
                    "",
                    "",
                    MessageConstants.MESSAGE_ANDROID_APPLICATION_EXIT_ACTION
                )
            }
        }
    }
}
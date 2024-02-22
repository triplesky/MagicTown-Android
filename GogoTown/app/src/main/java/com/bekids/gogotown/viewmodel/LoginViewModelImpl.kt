package com.bekids.gogotown.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingClient
import com.bekids.gogotown.BuildConfig
import com.bekids.gogotown.MainApplication
import com.bekids.gogotown.R
import com.bekids.gogotown.base.BaseViewModel
import com.bekids.gogotown.base.utils.IHEventBus
import com.bekids.gogotown.base.utils.MMKvUtils
import com.bekids.gogotown.bean.UserInfo
import com.bekids.gogotown.bean.LoginInfoHolder
import com.bekids.gogotown.bean.UserInfoHolder
import com.bekids.gogotown.network.bean.IHumanResult
import com.google.gson.Gson
import com.ihuman.library.accountsdk.AccountHelp
import com.bekids.gogotown.eventbus.BusMsg
import com.bekids.gogotown.ext.toast
import com.bekids.gogotown.model.bean.Product
import com.bekids.gogotown.model.bean.ProductListHolder
import com.bekids.gogotown.model.bean.ProductSkuBean
import com.bekids.gogotown.model.bean.ResumePurchaseBean
import com.bekids.gogotown.model.repository.LoginRepository
import com.bekids.gogotown.unity.bridge.bean.*
import com.ihuman.sdk.lib.account.IHAccountComponent
import com.ihuman.sdk.lib.model.FailMessage
import com.ihuman.sdk.lib.pay.IHPayComponent
import com.ihuman.sdk.lib.pay.api.callback.IHumanPayCallback
import com.ihuman.sdk.lib.pay.api.callback.IHumanQuerySkuCallback
import com.ihuman.sdk.lib.pay.api.callback.IHumanResumeAllPurchaseCallback
import com.ihuman.sdk.lib.pay.api.model.*
import com.ihuman.sdk.lib.thirdapi.model.ResultMessage
import com.ihuman.sdk.module.account.IHAccountUIComponent
import com.ihuman.sdk.module.account.bean.IHLoginConfig
import com.ihuman.sdk.module.account.bean.IHLoginUIConfig
import com.ihuman.sdk.module.account.callback.IHLoginFinishedCallback
import com.ihuman.sdk.module.account.callback.IHLoginHandleInfoCallback
import com.ihuman.sdk.module.account.constant.IHAccountLoginSupportType
import com.ihuman.sdk.module.account.constant.IHAccountLoginViewOrientationType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 ***************************************

 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/17    4:08 下午
 * 用途
 ***************************************

 */
class LoginViewModelImpl(private val loginRepository: LoginRepository) : BaseViewModel() {

    private val _loginUiState = MutableLiveData<LoginUIModel>()
    val loginUiState: LiveData<LoginUIModel>
        get() = _loginUiState


    private val _logoutUiState = MutableLiveData<LogoutUIModel>()
    val logoutUiState: LiveData<LogoutUIModel>
        get() = _logoutUiState

    fun login(context: Context) {
            val loginUIConfig = IHLoginUIConfig.Builder()
                .setBackgroundImage(R.drawable.bg_1)
                .setBackgroundViewCornerRadius(40f)
                .setCloseButtonImage(R.drawable.btn_skip)
                .setSubmitButtonImage(
                    R.drawable.btn_next_normal,
                    R.drawable.btn_next_disable
                )
                .setTitleTextColor(ContextCompat.getColor(context, R.color.title_color))
                .setPrivacyTextColor(
                    ContextCompat.getColor(context, R.color.privacy_text_color),
                    ContextCompat.getColor(context, R.color.privacy_text_color_high_light)
                )
                .setForgetPasswordButtonTitleColor(ContextCompat.getColor(context, R.color.forget_pwd))
                .setInputTextColors(
                    ContextCompat.getColor(context, R.color.input_text_color),
                    ContextCompat.getColor(context, R.color.input_background_color),
                    ContextCompat.getColor(context, R.color.input_placeholder_color)
                )
                .setInputClearImage(R.drawable.btn_clear)
                .setEmailLoginOrSignButton(ContextCompat.getColor(context, R.color.input_text_color),
                    ContextCompat.getColor(context, R.color.privacy_text_color_high_light)
                )
                .setResendCodeButtonTitleColor(
                    ContextCompat.getColor(context, R.color.white),
                    ContextCompat.getColor(context, R.color.white)
                )
                .setEmailConfirmButtonStyle(ContextCompat.getColor(context, R.color.white),
                    ContextCompat.getColor(context, R.color.input_text_color),
                    R.drawable.btn_create,
                    R.drawable.btn_create_disable)
                .setBackButtonImage(R.drawable.btn_back)
                .setInputSecureImages(R.drawable.btn_password_display, R.drawable.btn_password_show)
                .build()

            val loginConfig = IHLoginConfig.Builder()
                .setOrientationType(IHAccountLoginViewOrientationType.Landscape)
                .setSupportOverseas(IHAccountLoginSupportType.OnlyOverseas)
//            .setThirdChannels(
//                mutableListOf(
//                    IHChannelType.WEIXIN,
//                    IHChannelType.QQ,
//                    IHChannelType.HUAWEI
//                )
//            )
                .setLoginUIConfig(loginUIConfig)
                .build()
            IHAccountUIComponent.getInstance().showLoginViewWithConfig(context as FragmentActivity,
                loginConfig,
                object : IHLoginHandleInfoCallback {
                    override fun onResult(loginResult: ResultMessage) {
                        loginResult?.let {
                            launchOnIO {
                                val result = loginRepository.login(loginResult)
                                withContext(Dispatchers.Main) {
                                    if (result is IHumanResult.Success) {
                                        if (result.isOk()) {
                                            AccountHelp.getInstance().finishedLoginWithUtoken(result.result?.utoken?.utoken)
                                            LoginInfoHolder.saveUser(Gson().toJson(result.result), result.result)
                                            UserInfoHolder.saveUser(Gson().toJson(result.result?.user_info), result.result?.user_info)
                                            emitLoginUIState(userInfoBean = result.result)
                                        } else {
                                            IHAccountUIComponent.getInstance().failedLogin()
                                        }
                                    } else{
                                        IHAccountUIComponent.getInstance().failedLogin()
                                    }
                                }
                            }
                        }
                    }
                }, object : IHLoginFinishedCallback {
                    override fun onFinished(message: FailMessage) {
                        emitLoginUIState(showError = message.message)
                    }
                })


    }

    fun logout() {

        launchOnIO {
            //val result = loginRepository.logOut()
            withContext(Dispatchers.Main) {
//                if (result is IHumanResult.Success) {
//                    if (result.isOk()) {
                        LoginInfoHolder.clearUserInfo()
                        UserInfoHolder.clearUserInfo()
                        AccountHelp.getInstance().logoutWithBlock()
                        getUserInfo()
                        emitLogoutUIState(result = true)
//                    } else{
//                        emitLogoutUIState(result = false)
//                    }
//                } else {
//                    emitLogoutUIState(result = false)
//                }
            }

        }
    }

    fun getUserInfo(){
        launchOnIO {
            val result = loginRepository.getUserInfo()
            withContext(Dispatchers.Main){
                if (result is IHumanResult.Success) {
                    if (result.isOk()) {
                        UserInfoHolder.saveUser(Gson().toJson(result.result), result.result)
                        IHEventBus.instance?.post(BusMsg(BusMsg.GET_USER_INFO_COMPLETE, null))
                    }
                }
            }
        }
    }



    fun getProductList(){
        launchOnIO {
            val result = loginRepository.getProductList()
            withContext(Dispatchers.Main){
                if (result is IHumanResult.Success) {
                    if (result.isOk()) {
                        var list = mutableListOf<String>()
                        var product_list = mutableListOf<Product>()
                        result.result?.product_list?.let {
                            product_list = it as MutableList<Product>
                        }
                        result.result?.product_list?.forEach {
                            list.add(it.product_id)
                        }
                        MMKvUtils.put("user_status"+BuildConfig.VERSION_CODE, result.result!!.status)
                        //IHEventBus.instance?.post(BusMsg(BusMsg.PAY_FETCH_SERVER_PRODUCT_LIST_COMPLETE, null))
                        AccountHelp.getInstance().querySkuDetails(MainApplication.mainActivity, list,ProductType.SUBSCRIPTION,
                            object : IHumanQuerySkuCallback {
                                override fun onResult(skuLists: MutableList<SkuDetails>?) {
                                    if (skuLists != null) {
                                        var prosku =  ProductSkuBean(result.result!!, skuLists)

                                        ProductListHolder.saveProduct(Gson().toJson(prosku), prosku)
                                        if (!skuLists.isNullOrEmpty()) {

                                            var l = mutableListOf<PurchaseBean>()
                                            for ((i, item) in product_list.withIndex()) {
                                                if (skuLists.size == product_list.size) {
                                                    if (item.product_id == skuLists[i].sku) {
                                                        var data = PurchaseBean(item.all_pack_discount, item.product_id, item.product_type,
                                                            item.rights_type, Ih(skuLists[i].price, skuLists[i].sku,
                                                                (skuLists[i].priceAmountMicros /10000).toInt()
                                                            ))
                                                        l.add(data)
                                                    }
                                                }
                                            }

                                            IHEventBus.instance?.post(
                                                BusMsg(
                                                    BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST_COMPLETE,
                                                    l
                                                )
                                            )
                                        } else {
                                            IHEventBus.instance?.post(
                                                BusMsg(
                                                    BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST_COMPLETE,
                                                    null
                                                )
                                            )
                                        }
                                    } else{
                                        Log.i("lldebug sdk", "null")
                                        IHEventBus.instance?.post(
                                            BusMsg(
                                                BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST_COMPLETE,
                                                null
                                            )
                                        )
                                    }


                                }

                                override fun onError(code: Int, message: String?) {
                                    IHEventBus.instance?.post(
                                        BusMsg(
                                            BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST_COMPLETE,
                                            null
                                        )
                                    )
                                }

                            })
                    }else {
                        IHEventBus.instance?.post(
                            BusMsg(
                                BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST_COMPLETE,
                                null
                            )
                        )
                    }
                }else {
                    IHEventBus.instance?.post(
                        BusMsg(
                            BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST_COMPLETE,
                            null
                        )
                    )
                }
            }
        }
    }


    fun getUserStatus(){
        launchOnIO {
            val result = loginRepository.getProductList()
            withContext(Dispatchers.Main){
                if (result is IHumanResult.Success) {
                    if (result.isOk()) {
                        IHEventBus.instance?.post(BusMsg(BusMsg.PAY_FETCH_USER_STATUS_COMPLETE, CommonBean(0,"")))
                    }else {
                        IHEventBus.instance?.post(
                            BusMsg(
                                BusMsg.PAY_FETCH_USER_STATUS_COMPLETE,
                                CommonBean(result.code, result.msg)
                            )
                        )
                    }
                }else {
                    if (result is IHumanResult.Error) {
                        IHEventBus.instance?.post(
                            BusMsg(
                                BusMsg.PAY_FETCH_USER_STATUS_COMPLETE,
                                result.exception.message?.let { CommonBean(result.code, it) }
                            )
                        )
                    } else {
                        IHEventBus.instance?.post(
                            BusMsg(
                                BusMsg.PAY_FETCH_USER_STATUS_COMPLETE,
                                 CommonBean(-1, "exception")
                            )
                        )
                    }

                }
            }
        }
    }

    fun createOrder(product_id: String) {
        launchOnIO {
            val result = loginRepository.createOrder(product_id)
            withContext(Dispatchers.Main){
                if (result is IHumanResult.Success) {
                    if (result.isOk()) {
                        var payEneity = PayEneity()
                        payEneity.productId = product_id
                        payEneity.orderId = result.result?.order_id.toString()
                        payEneity.safeInfo = result.result?.safe_info
                        Log.i("lldebug safe info", payEneity.safeInfo + ", " +payEneity.orderId )
                        IHPayComponent.getInstance().payWithChannel(MainApplication.mainActivity, IHPayType.GOOGLE,  payEneity,
                            ProductType.SUBSCRIPTION, object : IHumanPayCallback {
                                override fun onFailure(failMessage: FailMessage?) {
                                    failMessage?.let {
                                        if (failMessage.code == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                            getUserInfo()
                                            IHPayComponent.getPayThirdPublicApi().resumeAllPurchases(MainApplication.mainActivity,
                                                object : IHumanResumeAllPurchaseCallback {
                                                    override fun resumePurchase(
                                                        code: Int,
                                                        msg: String?,
                                                        successProductIds: MutableList<String>?,
                                                        failProductIds: MutableList<String>?,
                                                    ) {
                                                        if (code == 0){
                                                            if (!successProductIds.isNullOrEmpty()) {
                                                                getUserInfo()
                                                                var data = ResumePayBean(0,successProductIds,"")
                                                                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
                                                            } else{
                                                                var data = ResumePayBean(0,null,"")
                                                                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
                                                            }
                                                        }else{
                                                            var data = Payment(0,V(0, ProductPay(product_id), 0,""))
                                                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, data))
                                                        }
                                                    }
                                                })

//                                            AccountHelp.getInstance().resumePurchaseForId(MainApplication.mainActivity, product_id,
//                                                object : IHumanResumePurchaseCallback{
//                                                    override fun resumePurchase(code: Int, msg: String?) {
//                                                        if (code == 0) {
//                                                            getUserInfo()
//                                                            var data = Payment(0,V(230, ProductPay(product_id), 0,""))
//                                                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
//                                                        } else {
//                                                            var data = Payment(0,V(0, ProductPay(product_id), 0,""))
//                                                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, data))
//                                                        }
//                                                    }
//
//                                                })

                                        } else {
                                            var data = Payment(failMessage.code,
                                                V(it.code, ProductPay(product_id), 0,""))
                                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, data))
                                        }

                                    }

                                }

                                override fun onSuccess(result: PayResultMessage?) {
                                    result?.let {
                                        getUserInfo()
                                        var data = Payment(0,V(0, ProductPay(product_id), it.deliverStatus, result.orderId))
                                        IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, data))
                                    }

                                }

                            })

                    } else {
                        var data = Payment(-1,
                            V(-1, ProductPay(product_id), 0,""))
                        IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, data))
                    }
                } else {
                    if (result is IHumanResult.Error) {
                        if (result.code == 110105) {
                            getUserInfo()
                            var data = Payment(0,V(0, ProductPay(product_id), 0,""))
                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, data))
                        } else{
                            var data = Payment(-1,
                                V(-1, ProductPay(product_id), 0,""))
                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, data))
                        }
                    } else{
                        var data = Payment(-1,
                            V(-1, ProductPay(product_id), 0,""))
                        IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, data))
                    }

                }
            }
        }
    }

    fun purchaseWithProductID(product_id: String){
        var payEneity = PayEneity()
        payEneity.productId = product_id
        IHPayComponent.getInstance().payWithChannel(MainApplication.mainActivity, IHPayType.GOOGLE,  payEneity,
            ProductType.CONSUMABLE, object : IHumanPayCallback{
                override fun onFailure(failMessage: FailMessage?) {
                    IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, -1))
                }

                override fun onSuccess(result: PayResultMessage?) {
                    getUserInfo()
                    IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE, null))
                }

            })
    }

    fun resumePurchase() {
        launchOnIO {

            IHPayComponent.getPayThirdPublicApi().resumeAllPurchases(MainApplication.mainActivity,
                object : IHumanResumeAllPurchaseCallback {
                    override fun resumePurchase(
                        code: Int,
                        msg: String?,
                        successProductIds: MutableList<String>?,
                        failProductIds: MutableList<String>?,
                    ) {
                        if (code == 0){
                            if (!successProductIds.isNullOrEmpty()) {
                                getUserInfo()
                                var data = ResumePayBean(0,successProductIds,"")
                                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
                            } else{
                                var data = ResumePayBean(0,null,"")
                                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
                            }
                        }else{
                            var data = msg?.let { ResumePayBean(-1,null, it) }
                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
                        }
                    }
                })
//            val result = loginRepository.getProductList()
//            withContext(Dispatchers.Main) {
//                if (result is IHumanResult.Success) {
//                    if (result.isOk()) {
//                        result.result?.let {
//                            if (!it.product_list.isNullOrEmpty()) {
//                                for ((i, pro) in it.product_list.withIndex()) {
//                                    AccountHelp.getInstance().resumePurchaseForId(MainApplication.mainActivity, pro.product_id,
//                                        object : IHumanResumePurchaseCallback{
//                                            override fun resumePurchase(code: Int, msg: String?) {
//                                                if (code == 0) {
//                                                    getUserInfo()
//                                                    var data = Payment(0,V(230, ProductPay(pro.product_id), 0,""))
//                                                    IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
//                                                }else{
//                                                    var data = Payment(-1,
//                                                        V(-1, ProductPay(pro.product_id), -1,""))
//                                                    IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
//                                                }
//                                            }
//
//                                        })
//                                }
//
//                            }
//                        }
//                    } else{
//                        if (ProductListHolder.getProduct() != null) {
//                            ProductListHolder.getProduct()?.let {
//                                var data = Payment(-1,
//                                    V(-1, ProductPay(it.product_list[0].product_id), -1,""))
//                                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
//                            }
//                        } else {
//                            var data = Payment(-1,
//                                V(-1, ProductPay("-1"), -1,""))
//                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
//                        }
//
//                    }
//                } else {
//                    if (ProductListHolder.getProduct() != null) {
//                        ProductListHolder.getProduct()?.let {
//                            var data = Payment(-1,
//                                V(-1, ProductPay(it.product_list[0].product_id), -1,""))
//                            IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
//                        }
//                    } else {
//                        var data = Payment(-1,
//                            V(-1, ProductPay("-1"), -1,""))
//                        IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE_COMPLETE, data))
//                    }
//
//                }
//            }
        }
    }

    private fun emitLogoutUIState(
        result: Boolean? = null,
        showError: String? = null
    ) {
        val uiModel =
            LogoutUIModel(
                result = result,
                showError = showError
            )
        _logoutUiState.value = uiModel
    }

    /**
     *
     * @param userInfoBean AccountInfo?
     * @param showError String?
     */
    private fun emitLoginUIState(
        userInfoBean: UserInfo? = null,
        showError: String? = null
    ) {
        val uiModel =
            LoginUIModel(
                userInfoBean = userInfoBean,
                showError = showError
            )
        _loginUiState.value = uiModel
    }


    data class LoginUIModel(
        val userInfoBean: UserInfo?,
        val showError: String?
    )

    data class LogoutUIModel(
        val result: Boolean?,
        val showError: String?
    )




}
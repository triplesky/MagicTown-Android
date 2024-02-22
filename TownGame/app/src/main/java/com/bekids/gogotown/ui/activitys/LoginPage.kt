package com.bekids.gogotown.ui.activitys

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bekids.gogotown.R
import com.ihuman.library.accountsdk.AccountHelp
import com.ihuman.sdk.lib.model.FailMessage
import com.ihuman.sdk.lib.thirdapi.channel.IHChannelType
import com.ihuman.sdk.lib.thirdapi.model.ResultMessage
import com.ihuman.sdk.module.account.IHAccountUIComponent
import com.ihuman.sdk.module.account.bean.IHLoginConfig
import com.ihuman.sdk.module.account.bean.IHLoginUIConfig
import com.ihuman.sdk.module.account.callback.IHLoginFinishedCallback
import com.ihuman.sdk.module.account.callback.IHLoginHandleInfoCallback
import com.ihuman.sdk.module.account.constant.IHAccountLoginSupportType
import com.ihuman.sdk.module.account.constant.IHAccountLoginViewOrientationType

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/7/22    4:30 下午
 * 用途
 ***************************************

 */
object LoginPage {
    fun init(context: Context) {
        val loginUIConfig = IHLoginUIConfig.Builder()
            .setBackgroundImage(R.drawable.bg_1)
            .setBackgroundViewCornerRadius(40f)
            .setCloseButtonImage(R.drawable.btn_skip)
            .setTitleTextColor(ContextCompat.getColor(context, R.color.title_color))
            .setSubmitButtonImage(
                R.drawable.btn_02,
                R.drawable.btn_02
            )
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
            .setCreateEmailAccountButtonUI(ContextCompat.getColor(context, R.color.white), R.drawable.btn_create)
            .setResendCodeButtonTitleColor(
                ContextCompat.getColor(context, R.color.white),
                ContextCompat.getColor(context, R.color.white)
            )
            .setBackButtonImage(R.drawable.btn_back)
            .setInputSecureImages(R.drawable.btn_password_display, R.drawable.btn_password_show)
            .build()

        val loginConfig = IHLoginConfig.Builder()
            .setOrientationType(IHAccountLoginViewOrientationType.Landscape)
            .setSupportOverseas(IHAccountLoginSupportType.MainlandAndOverseas)
            .setThirdChannels(
                mutableListOf(
                    IHChannelType.WEIXIN,
                    IHChannelType.QQ,
                    IHChannelType.HUAWEI
                )
            )
            .setLoginUIConfig(loginUIConfig)
            .build()
        IHAccountUIComponent.getInstance().showLoginViewWithConfig(context as FragmentActivity,
            loginConfig,
            object : IHLoginHandleInfoCallback {
                override fun onResult(loginResult: ResultMessage) {
                    // do something
                }

            },
            object : IHLoginFinishedCallback {
                override fun onFinished(message: FailMessage) {
                    // do something
                }
            }
        )
    }
}
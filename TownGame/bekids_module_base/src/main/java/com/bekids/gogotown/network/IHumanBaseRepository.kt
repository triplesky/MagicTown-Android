package com.bekids.gogotown.network

import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.base.utils.IHEventBus
import com.ihuman.library.accountsdk.AccountHelp
import com.bekids.gogotown.bean.LoginInfoHolder
import com.bekids.gogotown.eventbus.BusMsg
import com.bekids.gogotown.network.exception.ExceptionManager
import com.bekids.gogotown.network.bean.IHumanResponse
import com.bekids.gogotown.network.exception.ApiException

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:18 下午
 * 用途
 ***************************************

 */
open class IHumanBaseRepository : BaseRepository() {
    override suspend fun hanldeException(result: Exception): Exception {
        return ExceptionManager.handleException(result)
    }

    override suspend fun <T> handleCode(response: IHumanResponse<T>): Any {
        BaseApplication.token_invalid = 0
        return if (correctCode(response) != 0) {
            when (correctCode(response)) {
                252 -> {

                    LoginInfoHolder.clearUserInfo()
                    AccountHelp.getInstance().logoutWithBlock()
                    if (BaseApplication.token_invalid == 0) {
                        BaseApplication.token_invalid = 1
                        IHEventBus.instance?.post(BusMsg(BusMsg.SIGN_OUT_COMPLETE,true))
                        IHEventBus.instance?.post(BusMsg(BusMsg.NOTI_USER_RIGHTS_UPDATE, null))
                    }
                    ApiException(correctCode(response), correctMessage(response))


                }
                else -> {BaseApplication.token_invalid = 0
                    ApiException(correctCode(response), correctMessage(response))
                }
            }

        } else {
            BaseApplication.token_invalid = 0
            response
        }
    }

    /**
     * 获取请求的service
     * @param clazz Class<S>
     * @return S
     */
    fun <S> getService(clazz: Class<S>): S = IHumanRetrofitClient.getService(clazz)
}
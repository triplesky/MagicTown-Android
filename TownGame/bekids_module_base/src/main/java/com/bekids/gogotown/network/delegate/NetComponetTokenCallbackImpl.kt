package com.bekids.gogotown.network.delegate

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/20    2:22 下午
 * 用途
 ***************************************

 */
class NetComponetTokenCallbackImpl : NetComponent.TokenCallback{
    override suspend fun refreshToken(): Boolean {
        return false
    }

    override fun showToast(message: String?) {
    }

    override fun handlerEvent(code: Int, message: String?) {
    }
}
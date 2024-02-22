package com.bekids.gogotown.network.exception

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:22 下午
 * 用途
 ***************************************

 */

data class ApiException(
    override var code: Int = -1,
    override val message: String = "",
    val displayMessage: String = "",
    val needRetry: Boolean = false
) : BaseException(isNeedRetry = needRetry)
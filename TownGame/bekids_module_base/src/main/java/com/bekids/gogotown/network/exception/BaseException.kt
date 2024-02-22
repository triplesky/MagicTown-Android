package com.bekids.gogotown.network.exception

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    12:00 下午
 * 用途
 ***************************************

 */
open class BaseException(override val message: String = "",
                         val isNeedRetry: Boolean = false,
                         open val code: Int = 0) : Exception(message)
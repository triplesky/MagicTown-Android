package com.bekids.gogotown.network.bean

import androidx.annotation.Keep

/**
 * 网络请求基本的返回数据封装
 * ，一切皆有可能，所以都可空。
 */
@Keep
data class IHumanResponse<out T>(
        val code: Int = 0,
        val message: String = "",
        val result: T? = null
)

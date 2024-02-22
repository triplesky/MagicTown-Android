package com.bekids.gogotown.network.bean

import androidx.annotation.Keep

/**
 ***************************************
 * 项目名称:${PROJECT_NAME}
 * @Author username
 * 邮箱：username@ksjgs.com
 * 创建时间: ${DATE}     ${TIME}
 * 用途
 ***************************************

 */

@Keep
sealed class IHumanResult<out T : Any> {

    data class Success<out T : Any>(val result: T? = null, val code: Int = 0, val msg: String = "") : IHumanResult<T>()
    data class Error(val code: Int = -1, val exception: Exception) : IHumanResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$result]"
            is Error -> "Error[exception=$exception]"
        }
    }

    /**
     * 请求数据 正确返回，没有异常code
     */
    fun isOk(): Boolean {
        if (this is Success) {
            return code == 0
        }
        return false
    }
}
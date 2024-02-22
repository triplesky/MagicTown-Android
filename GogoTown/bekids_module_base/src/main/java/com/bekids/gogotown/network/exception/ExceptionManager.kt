package com.bekids.gogotown.network.exception

import android.net.ParseException
import com.bekids.gogotown.base.BaseApplication
import com.google.gson.JsonParseException
import com.bekids.gogotown.ext.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    2:21 下午
 * 用途
 ***************************************

 */
object ExceptionManager {
    /**
     * 未知错误
     */
    const val UNKNOWN = 2000

    /**
     * 解析错误
     */
    const val PARSE_ERROR = 2001

    /**
     * 网络连接错误
     */
    const val NETWORK_ERROR = 2002

    /**
     * 网络超时或者找不到Host错误
     */
    const val HTTP_UNKNOW_HOST_SOCKET_TIME_OUT_ERROR = 2003

    /**
     * 数据错误
     */
    const val DATA_ERROR = 2004

    /**
     * 参数错误
     */
    const val PARAM_ERROR = 2005
    const val HTTP_ERROR = 2006
    const val TOKEN_ERROR = 2019

    /**
     * 无网络
     */

    const val NO_NETWORK_ERROR = 2007

    /**
     * 异常处理
     * @param e Throwable
     * @return ApiException
     */
    suspend fun handleException(e: Throwable): ApiException {
        var ex: ApiException?
        if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {
            //解析错误
            ex = ApiException(PARSE_ERROR, "数据走丢了，请重试")
        } else if (e is ConnectException) {
            //网络错误
            ex = ApiException(NETWORK_ERROR, "连接超时，请重试")
        } else if (e is UnknownHostException || e is SocketTimeoutException) {
            //连接错误
            ex = ApiException(HTTP_UNKNOW_HOST_SOCKET_TIME_OUT_ERROR, "连接超时，请重试")
        } else if (e is HttpException) {
            ex = ApiException(UNKNOWN, e.message ?: "")
        } else if (e is ApiException) {
            //未知错误
            ex = ApiException(e.code, e.message)
        } else {
            ex = ApiException(UNKNOWN, e.message ?: "")
        }
        showMsg(ex.message)
        return ex
    }

    /**
     *  toast提示
     * @param msg String?
     */
    private suspend fun showMsg(msg: String?) {
//        withContext(Dispatchers.Main) {
//            BaseApplication.context.toast("$msg")
//        }
    }
}
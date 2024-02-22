package com.bekids.gogotown.network

import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.network.exception.BaseException
import com.bekids.gogotown.network.bean.IHumanResponse
import com.bekids.gogotown.network.bean.IHumanResult
import com.bekids.gogotown.network.exception.ApiException
import com.bekids.gogotown.network.exception.ExceptionManager
import com.ihuman.sdk.lib.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    11:57 上午
 * 用途 基础网络请求封装
 ***************************************

 */
abstract class BaseRepository {
    suspend fun <T : Any> safeApiCall(
        call: suspend () -> IHumanResult<T>,
        errorMessage: String? = null
    ): IHumanResult<T> {
          return try {
            if (!NetworkUtils.isConnected()) {
                return handleException(ApiException(ExceptionManager.NO_NETWORK_ERROR, "网络异常"), errorMessage, call)
            }
            val result = call()
            if (result is IHumanResult.Error) {
                return handleException(result.exception, errorMessage, call)
            }
            return result
        } catch (e: Exception) {
            // An exception was thrown when calling the API so we're converting this to an IOException
            var error = IHumanResult.Error(exception = e)
            return handleException(error.exception, errorMessage, call)
        }
    }

    /**
     * 处理 异常，加入了 是否重试 选项
     * @param exception Exception 异常类
     * @param errorMessage String? 错误信息
     * @param call SuspendFunction0<KsResult<T>> ，重试需要调用的方法
     * @return KsResult<T> 返回结果
     */
    private suspend fun <T : Any> handleException(
        exception: Exception,
        errorMessage: String?,
        call: suspend () -> IHumanResult<T>
    ): IHumanResult<T> {

        return if (exception is BaseException) {
            if (exception.isNeedRetry) {
                call()
            } else {
                var error = IHumanResult.Error(code = exception.code, exception = exception)
                hanldeException(error.exception)
                error
            }

        } else {
            var error = IHumanResult.Error(exception = exception)
            hanldeException(error.exception)
            error
        }
    }

    /**
     * 处理从网络获取的数据
     * @param
     */
    suspend fun <T : Any> executeResponse(
        response: IHumanResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): IHumanResult<T> {
        return coroutineScope {
            //
            when (val result = handleCode(response)) {
                is Exception -> {
                    errorBlock?.let { it() }
                    IHumanResult.Error(correctCode(response), result)
                }
                else -> {
                    successBlock?.let { it() }
                    IHumanResult.Success(
                        result = correctData(response),
                        code = correctCode(response),
                        msg = correctMessage(response)
                    )
                }
            }
        }
    }

    abstract suspend fun hanldeException(result: Exception): Exception

    /**
     * 处理网络请求错误code和业务code
     * @param response KsResponse<T>
     * @return Any
     */
    abstract suspend fun <T> handleCode(response: IHumanResponse<T>): Any

    /**
     * 兼容后端，转换code
     * @param response KsResponse<T>
     * @return Int
     */
    fun <T> correctCode(response: IHumanResponse<T>): Int {
        return response.code
    }

    /**KsResponse
    KsResult
     * 兼容后端，转换message
     * @param response KsResponse<T>
     * @return String
     */
    fun <T> correctMessage(response: IHumanResponse<T>): String {
        return response.message
    }

    /**
     * 兼容后端，转换data
     * @param response KsResponse<T>
     * @return T?
     */
    fun <T> correctData(response: IHumanResponse<T>): T? = with(response) {
        result
    }
}
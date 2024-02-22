package com.bekids.gogotown.network

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 ***************************************
 * 项目名称: bekids Town
 * @Author ll
 * 创建时间: 2022/10/18    6:02 下午
 * 用途
 ***************************************

 */
abstract class CoroutineViewModel : ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}
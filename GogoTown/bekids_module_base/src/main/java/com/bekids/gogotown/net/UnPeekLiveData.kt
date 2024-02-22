package com.bekids.gogotown.net

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.*

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/18    5:57 下午
 * 用途
 ***************************************

 */
class UnPeekLiveData <T> : MutableLiveData<T>() {

    private var isCleaning = false
    private var hasHandled = true
    private var isDelaying = false
    private val mTimer = Timer()
    private var mTask : TimerTask? = null
    private var DELAY_TO_CLEAR_EVENT = 1000

    // 仅存放 ObserverForever observers
    private val observers : MutableMap<androidx.lifecycle.Observer<in T>, androidx.lifecycle.Observer<T>> by lazy {
        mutableMapOf<androidx.lifecycle.Observer<in T>, androidx.lifecycle.Observer<T>>()
    }

    override fun observe(owner : LifecycleOwner, observer : androidx.lifecycle.Observer<in T>) {
        super.observe(owner, Observer {
            if (isCleaning) {
                hasHandled = true
                isDelaying = false
                isCleaning = false
                return@Observer
            }
            if (!hasHandled) {
                hasHandled = true
                isDelaying = true
                observer.onChanged(it)
            } else if (isDelaying) {
                observer.onChanged(it)
            }
        })
    }

    override fun observeForever(observer : androidx.lifecycle.Observer<in T>) {
        val wrap = Observer<T> {
            if (isCleaning) {
                hasHandled = true
                isDelaying = false
                isCleaning = false
                return@Observer
            }
            if (!hasHandled) {
                hasHandled = true
                isDelaying = true
                observer.onChanged(it)
            } else if (isDelaying) {
                observer.onChanged(it)
            }
        }
        observers.put(observer, wrap)
        super.observeForever(wrap)
    }

    override fun removeObserver(observer : androidx.lifecycle.Observer<in T>) {
        if (observers.containsKey(observer)) {
            observers.remove(observer)?.let {
                super.removeObserver(it)
            } ?: let { super.removeObserver(observer) }
        } else {
            super.removeObserver(observer)
        }
    }

    override fun removeObservers(owner : LifecycleOwner) {
        super.removeObservers(owner)
    }

    /**
     * 重写的 setValue 方法
     * @param value
     */
    override fun setValue(value : T?) {
        hasHandled = false
        isDelaying = false
        super.setValue(value)
        mTask?.let {
            it.cancel()
            mTimer.purge()
        }
        mTask = object : TimerTask() {
            override fun run() {
                clear()
            }
        }
        mTimer.schedule(mTask, DELAY_TO_CLEAR_EVENT.toLong())
    }

    private fun clear() {
        hasHandled = true
        isDelaying = false
    }
}
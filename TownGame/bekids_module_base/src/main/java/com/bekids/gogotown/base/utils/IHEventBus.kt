package com.bekids.gogotown.base.utils

import org.greenrobot.eventbus.EventBus

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/21    5:07 下午
 * 用途
 ***************************************

 */
object IHEventBus {

    @Volatile
    private var mInsance: EventBus? = null

    /**
     * 生成单例对象
     *
     * @return 单例对象
     */
    val instance: EventBus?
        get() {
            if (mInsance == null) {
                synchronized(EventBus::class.java) {
                    if (null == mInsance) {
                        mInsance = EventBus()
                    }
                }
            }
            return mInsance
        }

    /**
     * 注册事件接收者
     */
    @JvmStatic
    fun register(obj:Any){
        isRegistered(obj)?.let {
            if (!it) {
                mInsance?.register(obj)

            }
        }
    }

    /**
     * 取消注册对象接受者
     */
    @JvmStatic
    fun unregister(obj:Any){
        mInsance?.unregister(obj)
    }
    /**
     * 接收这是否已注册
     */
    @JvmStatic
    fun isRegistered(obj:Any):Boolean?{
        return mInsance?.isRegistered(obj)
    }

    /**
     * 清空所有粘性事件
     */
    @JvmStatic
    fun removeAllStickyEvents(){
        mInsance?.removeAllStickyEvents( )
    }
}
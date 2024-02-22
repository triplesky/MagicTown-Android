package com.bekids.gogotown.net

import androidx.annotation.IntDef

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/18    6:00 下午
 * 用途
 ***************************************

 */
class NetState constructor(var isSuccess : Boolean = true, @NetType var netType : Int = NetType.NET_TYPE_WIFI){


    fun getNetTypeStr():String{
        var type:String? = null
        when(netType){
            /**无网络*/
            NetType.NET_TYPE_NULL ->type="没有网络"

            /**正在建立连接*/
            NetType.NET_TYPE_CONNECTing ->type="正在连接"

            /**wifi*/
            NetType.NET_TYPE_WIFI ->type="WIFI"

            /**2G*/
            NetType.NET_TYPE_2G ->type="2G"

            /**3G*/
            NetType.NET_TYPE_3G ->type="3G"

            /**4G*/
            NetType.NET_TYPE_4G ->type="4G"

            /**5G*/
            NetType.NET_TYPE_5G ->type="5G"
            else  ->type="未知网络"
        }
        return type
    }

}

@IntDef(NetType.NET_TYPE_NULL, NetType.NET_TYPE_CONNECTing, NetType.NET_TYPE_UNKNOW, NetType.NET_TYPE_WIFI,
    NetType.NET_TYPE_2G, NetType.NET_TYPE_3G, NetType.NET_TYPE_4G, NetType.NET_TYPE_5G)
@Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY, AnnotationTarget.TYPE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class NetType {

    companion object {
        /**无网络*/
        const val NET_TYPE_NULL = 0

        /**正在建立连接*/
        const val NET_TYPE_CONNECTing = 0x1

        /**未知网络*/
        const val NET_TYPE_UNKNOW = 0x2

        /**wifi*/
        const val NET_TYPE_WIFI = 0x101

        /**2G*/
        const val NET_TYPE_2G = 0x102

        /**3G*/
        const val NET_TYPE_3G = 0x103

        /**4G*/
        const val NET_TYPE_4G = 0x104

        /**5G*/
        const val NET_TYPE_5G = 0x105


    }
}
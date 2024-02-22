package com.bekids.gogotown.eventbus

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/18    3:30 下午
 * 用途
 ***************************************

 */
class BusMsg<T : Any?> {
    var code: Int = 0
    var data: T? = null


    constructor(code: Int, data: T) {
        this.code = code
        this.data = data
    }

    override fun toString(): String {
        return "EventMessage{code=$code, data=$data}"
    }

    companion object{
        const val SIGN_IN = 0x30001
        const val SIGN_OUT = 0x30002
        const val CURRENT_ACCOUNT = 0x30003
        const val LAST_ACCOUNT = 0x30004
        const val GET_LOGIN_INFO = 0x30006
        const val REFRESH_USER_INFO = 0x30007

        const val SIGN_IN_COMPLETE = 0x30008
        const val SIGN_OUT_COMPLETE = 0x30009
        const val CURRENT_ACCOUNT_COMPLETE = 0x30010
        const val LAST_ACCOUNT_COMPLETE = 0x30011
        const val GET_LOGIN_INFO_COMPLETE = 0x30012
        const val GET_USER_INFO_COMPLETE = 0x30013
        const val NOTI_USER_RIGHTS_UPDATE = 0x30014

        const val PAY_FETCH_ALL_PACK_PRODUCT_LIST = 0x30015
        const val PAY_FETCH_ALL_PACK_PRODUCT_LIST_COMPLETE = 0x30016
        const val PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID = 0x30017
        const val PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE = 0x30018
        const val PAY_RESTORE_PURCHASE = 0x30019
        const val PAY_RESTORE_PURCHASE_COMPLETE = 0x30020
        const val PAY_PURCHASE_WITH_PRODUCT_ID = 0x30021
        const val PAY_FETCH_SERVER_PRODUCT_LIST_COMPLETE = 0x30022
        const val PAY_FETCH_USER_STATUS = 0x30023
        const val PAY_FETCH_USER_STATUS_COMPLETE = 0x30024
        const val MONGO_MERGE_WITH_DATA = 0x30025
        const val APP_EXIT = 0x30026


    }
}
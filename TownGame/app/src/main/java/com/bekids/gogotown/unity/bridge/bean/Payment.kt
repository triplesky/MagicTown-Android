package com.bekids.gogotown.unity.bridge.bean

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/9/14    10:46 上午
 * 用途
 ***************************************

 */
data class Payment(
    val code :Int,
    val payments:V
)
data class V(
    val status: Int,
    val product: ProductPay,
    val deliverStatus: Int,
    val orderId : String
)

data class ProductPay(val product_id:String)
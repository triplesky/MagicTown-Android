package com.bekids.gogotown.unity.bridge.bean

/**
 ***************************************
 * 项目名称: bekids Town
 * @Author ll
 * 创建时间: 2022/10/20    3:46 下午
 * 用途
 ***************************************

 */
data class PurchaseBean(
    val all_pack_discount: Double,
    val product_id: String,
    val product_type: Int,
    val rights_type: String,
    val ih_product:Ih
)

data class Ih(val priceString:String, val productId:String, val priceInCent:Int)
//priceInCent mPriceAmountMicros
//discountType
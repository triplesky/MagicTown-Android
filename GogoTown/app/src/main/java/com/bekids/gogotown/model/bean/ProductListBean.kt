package com.bekids.gogotown.model.bean

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/7/28    11:27 上午
 * 用途
 ***************************************

 */
data class ProductListBean(
    val product_list: List<Product>,
    val status: Int
)

data class Product(
    val legacy_product_id: String,
    val payable_amount: Int,
    val product_desc: String,
    val product_id: String,
    val product_name: String,
    val product_type: Int,
    val rights_type: String,
    val all_pack_discount : Double
)
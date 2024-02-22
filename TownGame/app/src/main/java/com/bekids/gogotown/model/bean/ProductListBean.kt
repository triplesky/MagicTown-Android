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
    val app_config_list: List<AppConfig>,
    val product_list: List<Product>,
    val status: Int,
    val ui_type: Int
)

data class AppConfig(
    val app_name: String,
    val app_name_en: String,
    val app_package_name: String,
    val appid: Int,
    val apple_id: String,
    val button: Int,
    val color: String,
    val detail_button: Int,
    val detail_list: List<Detail>,
    val game_id: String,
    val icon: String,
    val status: Int,
    val thumb_cover: String
)

data class Product(
    val discount: Int,
    val price: Int,
    val product_config: ProductConfig,
    val product_id: String,
    val product_type: Int,
    val rights_type: String
)

data class Detail(
    val image_url: String,
    val video_url: String
)

data class ProductConfig(
    val app_package_name: String,
    val appid: Int,
    val apple_id: String,
    val button: Int,
    val detail_list: List<DetailX>,
    val image_interval: Int
)

data class DetailX(
    val image_url: String
)

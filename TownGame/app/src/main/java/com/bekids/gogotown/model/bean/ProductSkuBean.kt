package com.bekids.gogotown.model.bean


import com.ihuman.sdk.lib.thirdapi.pay.model.SkuDetails

/**
 ***************************************
 * 项目名称: bekids Town
 * @Author ll
 * 创建时间: 2022/10/20    5:20 下午
 * 用途
 ***************************************

 */
data class ProductSkuBean(val productListBean: ProductListBean, val skuLists:List<SkuDetails>)

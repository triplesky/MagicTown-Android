package com.bekids.gogotown.model.bean

import android.util.Log
import androidx.annotation.Nullable
import com.bekids.gogotown.base.utils.IHvPreference
import com.bekids.gogotown.bean.UserInfo
import com.google.gson.Gson
import com.ihuman.sdk.lib.thirdapi.pay.model.SkuDetails

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/8/3    4:35 下午
 * 用途
 ***************************************

 */
object ProductListHolder {
    private const val PRODUCT_LIST = "product_list"
    var productJson: String by IHvPreference(PRODUCT_LIST, "")
    private var productInfo: ProductSkuBean? = null

    fun init() {

        //userJson 内容为空或者解析异常，userBean都置为null
        productInfo = try {
            Gson().fromJson(productJson, ProductSkuBean::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    @Nullable
    fun getProduct(): ProductListBean? {
        if (productInfo == null) {
            productInfo = Gson().fromJson(productJson, ProductSkuBean::class.java)
        }
        return productInfo?.productListBean
    }


    fun getSkuInfo():List<SkuDetails>?{
        if (productInfo == null) {
            productInfo = Gson().fromJson(productJson, ProductSkuBean::class.java)
        }
        return productInfo?.skuLists
    }
    fun getProductInfo(right_type : String):Product?{
        if (productInfo == null) {
            productInfo = Gson().fromJson(productJson, ProductSkuBean::class.java)
        }
        var product : Product? = null
        productInfo?.productListBean?.product_list?.let {
            for (item in it) {
                if (item.rights_type == right_type) {
                    product = item
                }
            }
        }
        return product
    }


    fun saveProduct(json: String, jsonBean: ProductSkuBean?=null) {
        try {
            var l = System.currentTimeMillis()
            productJson = json
            productInfo = jsonBean

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun clearProduct() {
        productJson = ""
        productInfo = null
    }

    fun log() {
        Log.i("lldebug", "userInfo: " + productJson)
    }
}
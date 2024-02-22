package com.bekids.gogotown.model.repository

import com.bekids.gogotown.bean.UserInfo
import com.bekids.gogotown.bean.UserInfoX
import com.bekids.gogotown.bean.UserRightsBean
import com.bekids.gogotown.model.bean.OrderBean
import com.bekids.gogotown.model.bean.ProductListBean
import com.bekids.gogotown.network.bean.IHumanResult
import com.ihuman.sdk.lib.thirdapi.model.ResultMessage

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/17    5:22 下午
 * 用途
 ***************************************

 */
interface LoginRepository {
    suspend fun login(result : ResultMessage) : IHumanResult<UserInfo>
    suspend fun logOut() : IHumanResult<Boolean>
    suspend fun getUserInfo() : IHumanResult<UserInfoX>
    suspend fun getProductList() : IHumanResult<ProductListBean>
    suspend fun createOrder(product_id : String) : IHumanResult<OrderBean>

}
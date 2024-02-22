package com.bekids.gogotown.model.api

import com.bekids.gogotown.bean.UserInfo
import com.bekids.gogotown.bean.UserInfoX
import com.bekids.gogotown.bean.UserRightsBean
import com.bekids.gogotown.model.bean.OrderBean
import com.bekids.gogotown.model.bean.ProductListBean
import com.bekids.gogotown.network.NetConstants
import com.bekids.gogotown.network.bean.IHumanResponse
import okhttp3.RequestBody
import retrofit2.http.*

/**
 ***************************************
 * 项目名称: gogo_town
 * @Author ll
 * 创建时间: 2022/7/25    2:34 下午
 * 用途
 ***************************************

 */
interface NetServices {
    /**
     * 验证码登录
     */
    @FormUrlEncoded
    @POST("/gogo_town/client/user/v1/login")
    suspend fun login(@FieldMap paramsMap: HashMap<String, String>): IHumanResponse<UserInfo>

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST("/gogo_town/client/user/v1/logout")
    suspend fun logOut(@FieldMap paramsMap: HashMap<String, String>): IHumanResponse<Boolean>

    /**
     * 获取权益
     */
    @FormUrlEncoded
    @POST("/gogo_town/client/user/v1/get_user_info")
    suspend fun getUserInfo(@FieldMap paramsMap: HashMap<String, String>): IHumanResponse<UserInfoX>

    /**
     * 验证utoken有效性
     */
    @FormUrlEncoded
    @POST("/gogo_town/client/user/utoken/verify")
    suspend fun verify(@FieldMap paramsMap: HashMap<String, String>): IHumanResponse<Boolean>

    /**
     * 查询商品列表
     */
    @FormUrlEncoded
    @POST("/gogo_town/client/store/v1/info")
    suspend fun getProductList(@FieldMap paramsMap: HashMap<String, String>): IHumanResponse<ProductListBean>

    /**
     * 创建订单
     */
    @FormUrlEncoded
    @POST("/gogo_town/client/order/v1/create_order")
    suspend fun createOrder(@FieldMap paramsMap: HashMap<String, String>): IHumanResponse<OrderBean>
}
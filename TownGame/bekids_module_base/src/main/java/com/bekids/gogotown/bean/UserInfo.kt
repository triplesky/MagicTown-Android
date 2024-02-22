package com.bekids.gogotown.bean

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/7/25    10:37 上午
 * 用途
 ***************************************

 */
data class UserInfo(
    val new_user: Int,
    val s: SS,
    val uid: Long,
    val user_info: UserInfoX,
    val utoken: Utoken
)

data class SS(
    val debug_sign_src: String,
    val kl: String,
    val sf: String,
    val sk: String
)

data class UserInfoX(
    val birthday: Int,
    val gender: Int,
    val head_image_url: String,
    val name: String,
    val rights: Rights,
    val s: SX
)

data class Utoken(
    val expire: Long,
    val uid: Long,
    val utoken: String
)

data class Rights(
    val all_pack: Int,
    val pack:List<String>,
    val vip: Int,
    val vip_expire_time: Int,
    val plus_vip: Int,
    val plus_vip_expire_time: Int
)

data class SX(
    val debug_sign_src: String,
    val kl: String,
    val sf: String,
    val sk: String
)
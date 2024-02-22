package com.bekids.gogotown.bean

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/7/25    10:43 上午
 * 用途
 ***************************************

 */
data class UserRightsBean(
    val s: S,
    val user_rights_vo: UserRightsVo
)

data class S(
    val debug_signsrc: String,
    val kl: String,
    val sf: String,
    val sk: String
)

data class UserRightsVo(
    val is_all_pack: Boolean,
    val is_vip: Boolean
)
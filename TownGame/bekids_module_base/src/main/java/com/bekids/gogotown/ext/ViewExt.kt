package com.bekids.gogotown.ext

import android.view.View

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/6/28    2:38 下午
 * 用途
 ***************************************

 */

fun View.click(action: () -> Unit) {
    setOnClickListener {
        action()
    }
}
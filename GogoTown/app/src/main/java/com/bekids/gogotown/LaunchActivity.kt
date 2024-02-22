package com.bekids.gogotown

import android.content.Intent
import com.ihuman.sdk.lib.utils.IHThreadUtils
import com.ihuman.sdk.module.launch.activity.IHEmptyLaunchActivity
import java.util.*


/**
 ***************************************
 * 项目名称: bekids Town
 * @Author ll
 * 创建时间: 2022/11/16    11:39 上午
 * 用途
 ***************************************

 */
class LaunchActivity : IHEmptyLaunchActivity() {

    override fun onDismiss() {
        super.onDismiss()
        IHThreadUtils.executeInMainThreadWithDelay(Runnable { val intent = Intent(this@LaunchActivity, MainActivity::class.java)
            startActivity(intent) }, 5000)

    }
}
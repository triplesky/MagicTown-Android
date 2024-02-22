package com.bekids.gogotown.ext

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes




/**
 * Created by luyao
 * on 2019/5/31 16:42
 */


fun Context.toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
    var innerContext = this
    if (this is Activity) {
        if (this.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && this.isDestroyed) {
            Log.e("ks", "show toast but Activity isDestroyed")
            innerContext = this.applicationContext
        }
    }
    Toast.makeText(innerContext, content, duration).apply {
        show()
    }
}

fun Context.toast(content: String) {
    Toast.makeText(this, content, Toast.LENGTH_SHORT).apply {
        show()
    }
}

fun Context.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), duration)
}

fun Context.longToast(content: String) {
    toast(content, Toast.LENGTH_LONG)
}

fun Context.longToast(@StringRes id: Int) {
    toast(id, Toast.LENGTH_LONG)
}

fun Any.toast(context: Context, content: String, duration: Int = Toast.LENGTH_SHORT) {
    context.toast(content, duration)
}

fun Any.toast(context: Context, @StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    context.toast(id, duration)
}

fun Any.longToast(context: Context, content: String) {
    context.longToast(content)
}

fun Any.longToast(context: Context, @StringRes id: Int) {
    context.longToast(id)
}



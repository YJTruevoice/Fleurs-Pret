package com.facile.immediate.electronique.fleurs.pret.common.ext

import android.os.SystemClock
import android.view.View
import com.facile.immediate.electronique.fleurs.pret.R

/**
 * * @Author Arthur
 * * @Data 2023/2/27
 * * @Description
 * ## View 点击扩展 防多次点击
 */


private val key: Int get() = R.id.tag_view_multi_click_control

private var <T : View> T.lastClickTime: Long
    get() {
        return (getTag(key) as? Long) ?: 0
    }
    set(value) {
        setTag(key, value)
    }

private fun <T : View> T.clickEnable(triggerDelay: Long = 800): Boolean {
    var flag = false
    val curClickTime = SystemClock.elapsedRealtime()
    if ((curClickTime - lastClickTime) >= triggerDelay) {
        flag = true
    }
    lastClickTime = curClickTime
    return flag
}

/**
 * 设置View的点击事件，防止多次点击
 */
fun <T : View> T.onClick(triggerDelay: Long = 800, block: (T) -> Unit) = setOnClickListener { view ->
    (view as? T)?.let {
        if (clickEnable(triggerDelay)) {
            block(it)
        }
    }
}
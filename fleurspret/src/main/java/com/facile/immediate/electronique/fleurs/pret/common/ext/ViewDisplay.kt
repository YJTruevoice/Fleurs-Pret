package com.facile.immediate.electronique.fleurs.pret.common.ext

import android.view.View
import androidx.viewbinding.ViewBinding


/**
 * 设置view显示
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * 设置view隐藏
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * 设置view占位隐藏
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrGone(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * 根据条件设置view显示占位隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrInvisible(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

/**
 * 判断ViewBinding是否已销毁
 */
fun ViewBinding?.checkViewBinding(): Boolean {
    return this != null
}
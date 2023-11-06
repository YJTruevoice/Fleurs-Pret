package com.facile.immediate.electronique.fleurs.pret.bottomsheet

import androidx.fragment.app.Fragment

/**
 *
 * created by guo.lei on 2022.08.26
 *
 */

/**
 * dialog行为
 */
interface IDialogPerformance {
    fun dismiss()
}

/**
 * dialog内容承载
 */
interface IDialogContent: IDialogPerformance {
    /**
     * 当前承载内容的fragment，一般return this，主要是因为接口中无法获取this对象
     */
    val current: Fragment?

    /**
     * 默认实现，dialog内容fragment调用dismiss需要调用到他所在的dialog容器，然后调用其对应的dismiss方法
     */
    override fun dismiss() {
        current?.let {
            (it.parentFragment as? IDialogPerformance)?.dismiss()
        }
    }
}
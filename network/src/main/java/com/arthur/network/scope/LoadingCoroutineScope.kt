package com.arthur.network.scope

import com.arthur.commonlib.ability.ILoading
import com.arthur.network.Net
import com.arthur.network.dialog.DefaultLoadingDialog
import com.arthur.network.runMain
import com.arthur.network.scope.builder.NetScopeConfig
import kotlinx.coroutines.CancellationException

/**
 *  * @Author Arthur
 *  * @Data 2023/4/25
 *  * @Description
 * ## 自动加载对话框网络请求
 *  * 开始: 显示对话框
 *  * 错误: 提示错误信息, 关闭对话框
 *  * 结束: 关闭对话框
 */
class LoadingCoroutineScope<T>(netConfig: NetScopeConfig<T>) : NetCoroutineScope<T>(netConfig) {
    private var dialog: ILoading? = Net.client.netOptions.loadingDialog

    override fun start() {
        super.start()
        if (netConfig.showLoading) {
            Net.client.netOptions.topActivityGetter?.invoke()?.let {
                if (dialog == null) {
                    dialog = DefaultLoadingDialog.NetLoading
                }
                dialog?.apply {
                    startLoading(it)
                }
            }
        }
    }

    override fun cache(result: T) {
        super.cache(result)
        if (netConfig.cacheBreakLoading) {
            dismiss()
        }
    }

    override fun finish(t: Throwable?) {
        super.finish(t)
        dismiss()
    }

    override fun error(t: Throwable?) {
        super.error(t)
        dismiss()
    }

    override fun cancel(cause: CancellationException?) {
        super.cancel(cause)
        dismiss()
    }

    private fun dismiss() {
        runMain {
            dialog?.closeLoading()
        }
    }
}
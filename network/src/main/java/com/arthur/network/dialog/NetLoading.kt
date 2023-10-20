package com.arthur.network.dialog

import android.app.Activity
import android.app.Dialog
import com.arthur.commonlib.ability.ILoading

object NetLoading : ILoading {

    private var loadingDialog: Dialog? = null

    override fun startLoading(ac: Activity) {
        startLoading(ac, "")
    }

    override fun startLoading(ac: Activity, message: String) {
        startLoading(ac, message, false)
    }

    fun startLoading(ac: Activity, message: String, cancelable: Boolean = false) {
        if (ac.isFinishing) {
            return
        }

        if (loadingDialog?.isShowing == true) {
            return
        } else {
            loadingDialog = createLoadingDialog(ac, message, cancelable)
        }
        loadingDialog?.show()
    }


    private fun createLoadingDialog(ac: Activity, message: String, cancelable: Boolean = false): Dialog {
        val loadingDialog = DefaultLoadingDialog.with(ac)
            .message(message)
            .cancelable(cancelable)
            .build().apply {
                setCanceledOnTouchOutside(false)
            }
        loadingDialog.setOwnerActivity(ac)
        return loadingDialog
    }

    override fun closeLoading() {
        try {
            if (loadingDialog != null && loadingDialog?.isShowing == true
                && loadingDialog?.ownerActivity != null && loadingDialog?.ownerActivity?.isFinishing == false
                && loadingDialog?.ownerActivity?.isDestroyed == false
            ) {
                loadingDialog?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            loadingDialog = null
        }
    }
}
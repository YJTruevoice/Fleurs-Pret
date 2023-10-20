package com.arthur.network.model

import android.os.Parcelable
import com.arthur.commonlib.utils.StringUtil
import kotlinx.parcelize.Parcelize

/**
 * 异常信息
 */
@Parcelize
open class ErrorInfo(
    /**
     * 异常类型，请使用[ErrorType]的枚举类型，如果有新增类型即在其基础上新增
     */
    open var errorType: ErrorType = ErrorType.CUSTOM_ERROR,

    /**
     * 异常码
     */
    open var errorCode: Int = -1,

    /**
     * 异常提示语
     */
    open var errorMsg: String = "",

    /**
     * 请求结果
     */
    open var result: String = ""

) : Parcelable {

    /**
     * 异常提示语
     */
    open val message: String
        get() = if (StringUtil.isEmpty(errorMsg)) {
            "发生错误（$errorCode）"
        } else {
            errorMsg
        }

}


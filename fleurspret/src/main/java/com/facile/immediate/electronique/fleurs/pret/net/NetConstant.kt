package com.facile.immediate.electronique.fleurs.pret.net

/**
 * @Author Arthur
 * @Data 2022/8/3
 * @Description
 * 网络库使用到的常量
 */
object NetConstant {

    // 打签的key
    const val KEY_SIGN = "sign"

    /**
     * 网络请求状态广播actions
     */
    object ServerStatusAction {
        // 网络请求状态action
        const val STATUS_ACTION = "com.facile.immediate.electronique.fleurs.pret.error_status_action"
    }

    /**
     * 网络请求状态 携带参数 key
     */
    object ServerStatusKey {
        const val CODE = "code"
        const val MSG = "msg"
        const val URL = "url"
    }

    /**
     * 网络请求成功码
     */
    object SuccessCode {
        const val CODE_SUCCESS = 1000
    }

    /**
     * 网络错误代码
     */
    object ErrorCode {

        /**
         * 登录失效
         */
        const val ERROR_CODE_TOKEN_INVALID = -1001

        /**
         * 其他错误
         */
        const val ERROR_CODE_OTHER = -1000

        // 自定义异常
        const val ERROR_API = 1008

        // 数据不合规异常
        const val ERROR_ILLEGAL_DATA = 1009

        // 需要弹出验证码
        const val NEED_CAPTCHA = 1125
    }

}
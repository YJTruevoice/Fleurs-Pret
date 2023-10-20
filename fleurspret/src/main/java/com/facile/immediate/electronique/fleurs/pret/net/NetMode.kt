package com.facile.immediate.electronique.fleurs.pret.net

import com.arthur.commonlib.utils.SPUtils

/**
 *  * @Author Arthur
 *  * @Data 2022/11/23
 *  * @Description
 * ## 网络调用环境
 */
object NetMode {

    private const val NET_ENV = "NET_ENV"
    var env: ENV = ENV.M //  0是正式环境， 1是测试环境 2是pre环境
        set(value) {
            SPUtils.putData(NET_ENV, value.env)
            field = value
        }
        get() {
            val e = SPUtils.getData(NET_ENV, ENV.M.env)
            return ENV.values().find { e == it.env } ?: ENV.M
        }

    fun isTest(): Boolean {
        return env != ENV.M
    }

    enum class ENV(val env: Int, val envName: String) {
        M(0, "RELEASE"),
        PRE(1, "PRE"),
        DEV(2, "DEV")
    }
}
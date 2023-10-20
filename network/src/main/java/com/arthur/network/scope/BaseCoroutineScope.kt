package com.arthur.network.scope

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 *  * @Author Arthur
 *  * @Data 2023/5/24
 *  * @Description
 * ## base CoroutineScope
 */
open class BaseCoroutineScope : CoroutineScope, Closeable {
    companion object {
        const val TAG: String = "BaseCoroutineScope"
    }

    override val coroutineContext: CoroutineContext = SupervisorJob()

    open fun launch() {}

    open fun cancel(cause: CancellationException? = null) {}

    override fun close() {
        cancel()
    }
}
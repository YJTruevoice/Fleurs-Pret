package com.arthur.baselib.structure.base.io

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

/**
 *
 * created by guo.lei on 2022.04.12
 * Kotlin支持的线程管理能力
 */
interface IKTIobility : IIObility {
    fun <T> launch(block: suspend () -> T?, handler: ((T?) -> Unit)?, error: ((Throwable) -> Unit)? = null): Job
    fun <T> launchFlow(block: suspend () -> T): Flow<T>
    suspend fun <T> withMain(block: suspend () -> T): T
    suspend fun <T> withIO(block: suspend () -> T): T
}
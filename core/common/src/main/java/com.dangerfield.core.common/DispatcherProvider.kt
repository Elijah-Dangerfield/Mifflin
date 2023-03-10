package com.dangerfield.core.common

import kotlinx.coroutines.CoroutineDispatcher

/**
 * interface to allow for the inject of coroutine dispatchers
 */
interface DispatcherProvider {
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}

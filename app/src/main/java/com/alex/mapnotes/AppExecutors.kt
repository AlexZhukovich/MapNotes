package com.alex.mapnotes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

const val NETWORK_THREAD_POOL = 3

open class AppExecutors(
    val ioContext: CoroutineContext = Dispatchers.Default,
    val networkContext: CoroutineContext = Executors.newFixedThreadPool(NETWORK_THREAD_POOL).asCoroutineDispatcher(),
    val uiContext: CoroutineContext = Dispatchers.Main
)
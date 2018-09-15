package com.alex.mapnotes

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlin.coroutines.experimental.CoroutineContext

const val NETWORK_THREAD_POOL = 3

open class AppExecutors(
    val ioContext: CoroutineContext = Dispatchers.Default,
    val networkContext: CoroutineContext = newFixedThreadPoolContext(NETWORK_THREAD_POOL, "network context"),
    val uiContext: CoroutineContext = Dispatchers.Main
)
package com.alex.mapnotes

import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlin.coroutines.experimental.CoroutineContext

const val NETWORK_THREAD_POOL = 3

open class AppExecutors(
        val ioContext: CoroutineContext = DefaultDispatcher,
        val networkContext: CoroutineContext = newFixedThreadPoolContext(NETWORK_THREAD_POOL, "network context"),
        val uiContext: CoroutineContext = UI)
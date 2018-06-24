package com.alex.mapnotes.ext

import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext

fun launch(
        context: CoroutineContext = DefaultDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        parent: Job? = null,
        onCompletion: CompletionHandler? = null,
        block: suspend CoroutineScope.() -> Unit) {
        kotlinx.coroutines.experimental.launch(context, start, parent, onCompletion, block)
}
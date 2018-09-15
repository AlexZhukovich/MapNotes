package com.alex.mapnotes.ext

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.CompletionHandler
import kotlinx.coroutines.experimental.CoroutineScope
import kotlin.coroutines.experimental.CoroutineContext

fun launch(
    context: CoroutineContext = Dispatchers.Default,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    parent: Job? = null,
    onCompletion: CompletionHandler? = null,
    block: suspend CoroutineScope.() -> Unit
) {
    kotlinx.coroutines.experimental.launch(context, start, parent, onCompletion, block)
}
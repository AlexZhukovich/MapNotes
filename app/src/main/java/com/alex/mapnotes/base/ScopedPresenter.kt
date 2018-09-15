package com.alex.mapnotes.base

import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

abstract class ScopedPresenter<V : MvpView> : MvpPresenter<V>, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    override fun onAttach(view: V?) {
        job = Job()
    }
    override fun onDetach() {
        job.cancel()
    }
}
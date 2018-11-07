package com.alex.mapnotes.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

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
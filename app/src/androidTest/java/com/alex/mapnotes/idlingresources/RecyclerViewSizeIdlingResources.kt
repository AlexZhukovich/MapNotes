package com.alex.mapnotes.idlingresources

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.alex.mapnotes.getIdlingResourceActivityInstance

class RecyclerViewSizeIdlingResources(@IdRes private val expectedViewId: Int) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return RecyclerViewSizeIdlingResources::class.java.simpleName
    }

    override fun isIdleNow(): Boolean {
        val view: RecyclerView? = getIdlingResourceActivityInstance().findViewById(expectedViewId)
        val isIdle = if (view != null) {
            view.adapter?.itemCount!! > 0
        } else {
            false
        }

        if (isIdle && resourceCallback != null) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }
}
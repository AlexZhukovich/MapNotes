package com.alex.mapnotes.idlingresources

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.IdlingResource
import com.alex.mapnotes.getIdlingResourceActivityInstance

class ViewVisibilityIdlingResource(
    @IdRes private val expectedViewId: Int,
    private val expectedViewVisibility: Int
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return ViewVisibilityIdlingResource::class.java.simpleName
    }

    override fun isIdleNow(): Boolean {
        val view: View? = getIdlingResourceActivityInstance().findViewById(expectedViewId)
        val isIdle = if (view != null) {
            view.visibility == expectedViewVisibility
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
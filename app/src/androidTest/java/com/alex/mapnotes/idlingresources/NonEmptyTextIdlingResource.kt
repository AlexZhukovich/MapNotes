package com.alex.mapnotes.idlingresources

import android.widget.TextView
import androidx.annotation.IdRes
import androidx.test.espresso.IdlingResource
import com.alex.mapnotes.getIdlingResourceActivityInstance

class NonEmptyTextIdlingResource(@IdRes private val expectedViewId: Int) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return NonEmptyTextIdlingResource::class.java.simpleName
    }

    override fun isIdleNow(): Boolean {
        val view: TextView? = getIdlingResourceActivityInstance().findViewById(expectedViewId)
        val isIdle = if (view != null) {
            !view.text.toString().isEmpty() && !view.text.toString().isBlank()
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
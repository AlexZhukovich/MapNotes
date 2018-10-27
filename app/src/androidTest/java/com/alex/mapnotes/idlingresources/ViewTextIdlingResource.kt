package com.alex.mapnotes.idlingresources

import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.IdlingResource
import com.alex.mapnotes.getIdlingResourceActivityInstance

class ViewTextIdlingResource(
    @IdRes private val expectedViewId: Int,
    @StringRes private val expectedViewText: Int
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return ViewTextIdlingResource::class.java.simpleName
    }

    override fun isIdleNow(): Boolean {
        val view: TextView? = getIdlingResourceActivityInstance().findViewById(expectedViewId)
        val expectedText = getIdlingResourceActivityInstance().getString(expectedViewText)
        val isIdle = if (view != null) {
            view.text.toString() == expectedText
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
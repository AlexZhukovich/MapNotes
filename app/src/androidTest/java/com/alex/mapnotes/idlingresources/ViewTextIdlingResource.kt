package com.alex.mapnotes.idlingresources

import android.app.Activity
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.IdlingResource
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

class ViewTextIdlingResource(
    @IdRes private val expectedViewId: Int,
    @StringRes private val expectedViewText: Int
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return ViewTextIdlingResource::class.java.simpleName
    }

    override fun isIdleNow(): Boolean {
        val view: TextView? = getActivityInstance().findViewById(expectedViewId)
        val expectedText = getActivityInstance().getString(expectedViewText)
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

    private fun getActivityInstance(): Activity {
        var currentActivity: Activity? = null
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        if (resumedActivities.iterator().hasNext()) {
            currentActivity = resumedActivities.iterator().next()
        }
        return currentActivity!!
    }
}
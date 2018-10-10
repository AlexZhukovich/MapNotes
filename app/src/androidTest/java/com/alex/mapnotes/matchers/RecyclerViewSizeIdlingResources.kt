package com.alex.mapnotes.matchers

import android.app.Activity
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

class RecyclerViewSizeIdlingResources(@IdRes private val expectedViewId: Int) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return RecyclerViewSizeIdlingResources::class.java.simpleName
    }

    override fun isIdleNow(): Boolean {
        val view: RecyclerView? = getActivityInstance().findViewById(expectedViewId)
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

    private fun getActivityInstance(): Activity {
        var currentActivity: Activity? = null
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        if (resumedActivities.iterator().hasNext()) {
            currentActivity = resumedActivities.iterator().next()
        }
        return currentActivity!!
    }
}
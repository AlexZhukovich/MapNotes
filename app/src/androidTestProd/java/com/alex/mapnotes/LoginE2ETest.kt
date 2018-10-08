package com.alex.mapnotes

import android.app.Activity
import android.view.View
import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage.RESUMED
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.login.LoginActivity
import com.alex.mapnotes.matchers.ViewTextIdlingResource
import com.alex.mapnotes.matchers.ViewVisibilityIdlingResource
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext

@RunWith(AndroidJUnit4::class)
class LoginE2ETest {

    private val permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    private val activityRule = object : ActivityTestRule<LoginActivity>(LoginActivity::class.java) {
        override fun beforeActivityLaunched() {
            StandAloneContext.loadKoinModules(listOf(appModule))
            super.beforeActivityLaunched()
        }
    }

    @Rule
    @JvmField
    val chain: RuleChain = RuleChain
            .outerRule(activityRule)
            .around(permissionRule)

    private val mapVisibilityIdlingResource by lazy {
        ViewVisibilityIdlingResource(R.id.mapContainer, View.VISIBLE)
    }

    private val snackbarErrorTextIdlingResource by lazy {
        ViewTextIdlingResource(com.google.android.material.R.id.snackbar_text, R.string.error_user_cannot_be_authenticated)
    }

    @Test
    fun shouldVerifySuccessfulLogin() {
        val email = "test@test.com"
        val password = "test123"

        signInWithEmailAndPassword(email, password)
        verifyMapContainerOnScreen()
        signOut()
    }

    @Test
    fun shouldVerifyFailureLogin() {
        val email = "test@test.com"
        val password = "test-password"

        signInWithEmailAndPassword(email, password)
        verifySignInErrorMessage()
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        onView(withId(R.id.signIn))
                .perform(click())

        onView(withId(R.id.email))
                .perform(replaceText(email))
        onView(withId(R.id.password))
                .perform(replaceText(password))
        onView(withId(R.id.signIn))
                .perform(click())
    }

    private fun signOut() {
        openActionBarOverflowOrOptionsMenu(getActivityInstance())
        onView(withText(R.string.nav_sign_out_title))
                .perform(click())
    }

    private fun verifyMapContainerOnScreen() {
        IdlingRegistry.getInstance().register(mapVisibilityIdlingResource)

        onView(withId(R.id.mapContainer))
                .check(matches(isDisplayed()))

        IdlingRegistry.getInstance().unregister(mapVisibilityIdlingResource)
    }

    private fun verifySignInErrorMessage() {
        IdlingRegistry.getInstance().register(snackbarErrorTextIdlingResource)

        onView(withText(R.string.error_user_cannot_be_authenticated))
                .check(matches(isDisplayed()))

        IdlingRegistry.getInstance().unregister(snackbarErrorTextIdlingResource)
    }

    private fun getActivityInstance(): Activity {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync {
            val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED)
            if (resumedActivities.iterator().hasNext()) {
                currentActivity = resumedActivities.iterator().next()
            }
        }
        return currentActivity!!
    }
}
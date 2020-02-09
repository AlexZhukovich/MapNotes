package com.alex.mapnotes.robots

import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.splash.SplashActivity
import org.koin.core.context.loadKoinModules

fun splashScreen(func: SplashScreenRobot.() -> Unit) = SplashScreenRobot().apply { func() }

val splashActivityE2ETestRule = object : ActivityTestRule<SplashActivity>(SplashActivity::class.java, true, false) {
    override fun beforeActivityLaunched() {
        loadKoinModules(listOf(appModule))
        super.beforeActivityLaunched()
    }
}

val splashActivityMockTestRule = ActivityTestRule<SplashActivity>(SplashActivity::class.java, true, false)

class SplashScreenRobot : BaseTestRobot() {
    fun displayAsEntryPoint() {
        splashActivityE2ETestRule.launchActivity(null)
    }

    fun displayMockAsEntryPoint() {
        splashActivityMockTestRule.launchActivity(null)
    }
}
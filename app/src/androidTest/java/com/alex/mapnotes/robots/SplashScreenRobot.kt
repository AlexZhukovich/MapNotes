package com.alex.mapnotes.robots

import androidx.test.core.app.ActivityScenario
import com.alex.mapnotes.splash.SplashActivity

fun splashScreen(func: SplashScreenRobot.() -> Unit) = SplashScreenRobot().apply { func() }

class SplashScreenRobot : BaseTestRobot() {

    fun launch() {
        ActivityScenario.launch(SplashActivity::class.java)
    }
}
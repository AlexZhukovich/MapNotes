package com.alex.mapnotes.robots

import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import com.alex.mapnotes.FragmentTestActivity

fun testScreen(func: TestActivityRobot.() -> Unit) = TestActivityRobot().apply { func() }

class TestActivityRobot {

    fun launch(fragment: Fragment) {
        ActivityScenario.launch(FragmentTestActivity::class.java)
                .onActivity { it.setFragment(fragment) }
    }
}
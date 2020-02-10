package com.alex.mapnotes.robots

import androidx.fragment.app.Fragment
import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.FragmentTestActivity

fun testScreen(func: TestActivityRobot.() -> Unit) = TestActivityRobot().apply { func() }

val testFragmentActivity = ActivityTestRule(FragmentTestActivity::class.java)

class TestActivityRobot {

    fun attachFragment(fragment: Fragment) {
        testFragmentActivity.activity.setFragment(fragment)
    }
}
package com.alex.mapnotes

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

class MockTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return Instrumentation.newApplication(MockMapNotesApp::class.java, context)
    }
}
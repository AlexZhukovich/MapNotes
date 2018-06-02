package com.alex.mapnotes

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_add_note -> {
                message.setText(R.string.nav_add_note_title)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                message.setText(R.string.nav_map_title)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search_notes -> {
                message.setText(R.string.nav_search_notes_title)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}

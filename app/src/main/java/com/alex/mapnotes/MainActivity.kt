package com.alex.mapnotes

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.navigation
import kotlinx.android.synthetic.main.button_sheet.bottomSheet

class MainActivity : AppCompatActivity() {

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottomSheet)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_add_note -> {
                replaceBottomFragment(SaveNoteFragment())
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search_notes -> {
                replaceBottomFragment(SearchNotesFragment())
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceBottomFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.bottomSheetContainer, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}

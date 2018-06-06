package com.alex.mapnotes.home

import android.support.design.widget.BottomSheetBehavior
import com.alex.mapnotes.R
import com.alex.mapnotes.data.repository.UserRepository

class HomePresenter(private val userRepository: UserRepository) : HomeMvpPresenter {
    private var view: HomeView? = null

    override fun onAttach(view: HomeView?) {
        this.view = view
    }

    override fun handleNavigationItemClick(itemId: Int) : Boolean {
        when (itemId) {
            R.id.navigation_add_note -> {
                view?.updateMapInteractionMode(true)
                view?.displayAddNote()
                view?.updateNavigationState(BottomSheetBehavior.STATE_COLLAPSED)
                return true
            }
            R.id.navigation_map -> {
                view?.updateNavigationState(BottomSheetBehavior.STATE_HIDDEN)
                return true
            }
            R.id.navigation_search_notes -> {
                view?.updateMapInteractionMode(true)
                view?.displaySearchNotes()
                view?.updateNavigationState(BottomSheetBehavior.STATE_EXPANDED)
                return true
            }
        }
        return false
    }

    override fun showLocationPermissionRationale() {
        view?.let {
            it.showPermissionExplanationSnackBar()
            it.hideContentWhichRequirePermissions()
        }
    }

    override fun checkUser() {
        if (userRepository.getUser() == null) {
            view?.navigateToLoginScreen()
        }
    }

    override fun signOut() {
        userRepository.signOut()
        view?.navigateToLoginScreen()
    }

    override fun onDetach() {
        this.view = null
    }
}

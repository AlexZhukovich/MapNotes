package com.alex.mapnotes.home

import android.support.design.widget.BottomSheetBehavior
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.UserRepository
import kotlinx.coroutines.experimental.launch

class HomePresenter(
    private val appExecutors: AppExecutors,
    private val userRepository: UserRepository
) : HomeMvpPresenter {

    private var view: HomeView? = null

    override fun onAttach(view: HomeView?) {
        this.view = view
    }

    override fun handleNavigationItemClick(itemId: Int): Boolean {
        view?.let { view ->
            when (itemId) {
                R.id.navigation_add_note -> {
                    view.updateMapInteractionMode(true)
                    view.displayAddNote()
                    view.updateNavigationState(BottomSheetBehavior.STATE_COLLAPSED)
                    return true
                }
                R.id.navigation_map -> {
                    view.updateNavigationState(BottomSheetBehavior.STATE_HIDDEN)
                    return true
                }
                R.id.navigation_search_notes -> {
                    view.updateMapInteractionMode(true)
                    view.displaySearchNotes()
                    view.updateNavigationState(BottomSheetBehavior.STATE_EXPANDED)
                    return true
                }
                else -> throw IllegalArgumentException("Unknown itemId")
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
        view?.let {view ->
            launch(appExecutors.uiContext) {
                val currentUser = userRepository.getCurrentUser()
                when (currentUser) {
                    is Result.Error -> {
                        view.navigateToLoginScreen()
                    }
                }
            }
        }
    }

    override fun signOut() {
        launch(appExecutors.uiContext) {
            launch(appExecutors.ioContext) {
                userRepository.signOut()
            }.join()
            view?.navigateToLoginScreen()
        }
    }

    override fun onDetach() {
        this.view = null
    }
}

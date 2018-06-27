package com.alex.mapnotes.base

interface MvpPresenter<V : MvpView> {

    fun onAttach(view: V?)

    fun onDetach()
}
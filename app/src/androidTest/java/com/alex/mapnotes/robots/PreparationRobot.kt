package com.alex.mapnotes.robots

import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.model.AuthUser
import io.mockk.coEvery
import io.mockk.every
import java.lang.Exception

fun prepare(func: PreparationRobot.() -> Unit) = PreparationRobot().apply { func() }

class PreparationRobot {

    fun mockLocationProvider(provider: LocationProvider) {
        every { provider.startLocationUpdates() } answers { nothing }
        every { provider.stopLocationUpdates() } answers { nothing }
        every { provider.addUpdatableLocationListener(any()) } answers { nothing }
        every { provider.isLocationAvailable() } returns false
    }

    fun mockSuccessfulSignIn(userRepository: UserRepository, email: String, password: String) {
        val authUser = AuthUser("111111")
        coEvery { userRepository.signIn(email, password) } returns Result.Success(authUser)
        coEvery { userRepository.getCurrentUser() } returns Result.Success(authUser)
    }

    fun mockUnsuccessfulSignInWithException(userRepository: UserRepository) {
        coEvery { userRepository.signIn(any(), any()) } returns Result.Error(Exception("SignIn error"))
    }
}
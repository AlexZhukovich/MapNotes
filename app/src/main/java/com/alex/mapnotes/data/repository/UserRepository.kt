package com.alex.mapnotes.data.repository

import com.alex.mapnotes.data.Result
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    suspend fun signIn(email: String, password: String) : Result<Boolean>

    suspend fun signUp(email: String, password: String) : Result<Boolean>

    suspend fun signOut()

    suspend fun getCurrentUser() : Result<FirebaseUser>

    fun changeUserName(user: FirebaseUser, name: String)

    suspend fun getHumanReadableName(userId: String) : Result<String>

    suspend fun getUserIdFromHumanReadableName(userName: String) : Result<String>
}
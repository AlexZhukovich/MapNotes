package com.alex.mapnotes.data.repository

import com.alex.mapnotes.data.Result
import com.alex.mapnotes.model.AuthUser

interface UserRepository {

    suspend fun signIn(email: String, password: String): Result<AuthUser>

    suspend fun signUp(email: String, password: String): Result<AuthUser>

    suspend fun signOut()

    suspend fun getCurrentUser(): Result<AuthUser>

    suspend fun changeUserName(user: AuthUser, name: String)

    suspend fun getHumanReadableName(userId: String): Result<String>

    suspend fun getUserIdFromHumanReadableName(userName: String): Result<String>
}
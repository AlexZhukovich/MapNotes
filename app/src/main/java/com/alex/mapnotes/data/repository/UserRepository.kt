package com.alex.mapnotes.data.repository

import com.alex.mapnotes.data.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ValueEventListener

interface UserRepository {

    fun signIn(email: String, password: String, result: (Task<AuthResult>) -> Unit)

    fun signUp(email: String, password: String, result: (Task<AuthResult>) -> Unit)

    fun getUser() : FirebaseUser?

    fun signOut()

    fun changeUserName(user: FirebaseUser, name: String)

    fun getHumanReadableName(userId: String, listener: ValueEventListener)

    suspend fun getHumanReadableName(userId: String) : Result<String>

    fun getUserIdFromHumanReadableName(userName: String, listener: ValueEventListener)
}
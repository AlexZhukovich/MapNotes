package com.alex.mapnotes.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AuthRepository {

    fun signIn(email: String, password: String, result: (Task<AuthResult>) -> Unit)

    fun signUp(email: String, password: String, result: (Task<AuthResult>) -> Unit)
}
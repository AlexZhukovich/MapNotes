package com.alex.mapnotes.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthRepository : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override fun signIn(email: String, password: String, result: (Task<AuthResult>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(result)
    }

    override fun signUp(email: String, password: String, result: (Task<AuthResult>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(result)
    }

    override fun getUser() : FirebaseUser? {
        return auth.currentUser
    }
}
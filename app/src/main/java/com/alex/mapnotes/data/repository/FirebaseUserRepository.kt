package com.alex.mapnotes.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseUserRepository : UserRepository {
    private val usersPath = "users"
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun signIn(email: String, password: String, result: (Task<AuthResult>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(result)
    }

    override fun signUp(email: String, password: String, result: (Task<AuthResult>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(result)
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getUser() : FirebaseUser? {
        return auth.currentUser
    }

    override fun changeUserName(user: FirebaseUser, name: String) {
        val usersRef = database.getReference(usersPath)
        usersRef.child(user.uid).setValue(hashMapOf("name" to name))
    }

    override fun getHumanReadableName(userId: String, listener: ValueEventListener) {
        database.getReference(usersPath).child(userId).addListenerForSingleValueEvent(listener)
    }

    override fun getUserIdFromHumanReadableName(userName: String, listener: ValueEventListener) {
        database.getReference(usersPath)
                .orderByChild("name")
                .equalTo(userName)
                .addListenerForSingleValueEvent(listener)
    }
}
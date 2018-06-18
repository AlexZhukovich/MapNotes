package com.alex.mapnotes.data.repository

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseUserRepository(private val appExecutors: AppExecutors) : UserRepository {
    private val usersPath = "users"
    private val nameKey = "name"
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override suspend fun signIn(email: String, password: String): Result<Boolean> = withContext(appExecutors.networkContext) {
        suspendCoroutine<Result<Boolean>> {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResultTask ->
                it.resume(Result.Success(authResultTask.result.user != null))
            }
        }
    }

    override suspend fun signUp(email: String, password: String) : Result<Boolean> = withContext(appExecutors.networkContext) {
        suspendCoroutine<Result<Boolean>> {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {authResultTask ->
                it.resume(Result.Success(authResultTask.result != null))
            }
        }
    }

    override suspend fun signOut() = withContext(appExecutors.networkContext) {
        launch {
            auth.signOut()
        }.join()
    }

    override fun getUser() : FirebaseUser? {
        return auth.currentUser
    }

    override fun changeUserName(user: FirebaseUser, name: String) {
        val usersRef = database.getReference(usersPath)
        usersRef.child(user.uid).setValue(hashMapOf(nameKey to name))
    }

    override fun getHumanReadableName(userId: String, listener: ValueEventListener) {
        database.getReference(usersPath).child(userId).addListenerForSingleValueEvent(listener)
    }

    override suspend fun getHumanReadableName(userId: String) : Result<String> = withContext(appExecutors.networkContext) {
        suspendCoroutine<Result<String>> {
            database.getReference(usersPath).child(userId).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.resume(Result.Error(databaseError.toException()))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        it.resume(Result.Success(dataSnapshot.children.first().value.toString()))
                    }
                }
            })
        }
    }

    override suspend fun getUserIdFromHumanReadableName(userName: String) : Result<String> = withContext(appExecutors.networkContext) {
        suspendCoroutine<Result<String>> {
            database.getReference(usersPath)
                    .orderByChild(nameKey)
                    .equalTo(userName)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            it.resume(Result.Error(databaseError.toException()))
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                it.resume(Result.Success(dataSnapshot.children.first().key.toString()))
                            }
                        }
                    })
        }
    }
}
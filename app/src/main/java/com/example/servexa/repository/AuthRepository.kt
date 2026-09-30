package com.example.servexa.repository

import android.util.Log.e
import com.example.servexa.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    suspend fun login(
        email: String,
        password: String
    ): Result<FirebaseUser> {

        try {

            val result = auth
                .signInWithEmailAndPassword(
                    email,
                    password
                )
                .await()

            return Result.success(result.user!!)

        } catch (e: Exception) {

            return Result.failure(e)
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        role: String
    ): Result<Unit> {
        return try {
            val authResult = FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(
                    email, password
                )
                .await()

            val firebaseUser=authResult.user?:return Result.failure(
                Exception("User creation failed")
            )
            val user = User(
                uid = firebaseUser.uid,
                name=name,
                email=email,
                role=listOf(role),
                currentRole = role,
                services = emptyList()


            )
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()
            Result.success(Unit)
        }
        catch (e: Exception){
            Result.failure(e)
        }

    }
}

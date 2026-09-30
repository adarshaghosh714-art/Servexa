package com.example.servexa.repository

import com.example.servexa.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            firestore
                .collection("users")
                .document(user.uid)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(uid: String): Result<User> {

        return try {

            val document = firestore
                .collection("users")
                .document(uid)
                .get()
                .await()

            val user = document.toObject(User::class.java)

            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(
                    Exception("User data not found")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return null
        return try {
            val snapshot = FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateProfile(
        name: String,
        currentRole: String,
        roles: List<String>
    ): Result<Unit> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
                ?: return Result.failure(
                    Exception("User not logged in")
                )

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.uid)
                .update(
                    mapOf(
                        "name" to name,
                        "currentRole" to currentRole,
                        "roles" to roles
                    )
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateServices(services: List<String>): Result<Unit> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser ?: return Result.failure(
                Exception("User is not logged in")
            )
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.uid)
                .update("services",services)
                .await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
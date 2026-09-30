package com.example.servexa.repository

import com.example.servexa.model.Provider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProviderRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getProviderByServices(service: String): Result<List<Provider>>{
        return try {
            val snapshot = firestore.collection("users")
                .whereArrayContains("services",service)
                .get()
                .await()
            val providers = snapshot.documents.mapNotNull {
                it.toObject(Provider::class.java)
            }
            Result.success(providers)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
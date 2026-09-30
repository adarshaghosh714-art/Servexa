package com.example.servexa.repository

import com.example.servexa.model.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class JobRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private fun jobsCollection() = firestore.collection("jobs")

    suspend fun addJobs(job: Job): Result<Unit> {
        return try {
            val document = jobsCollection().document()
            val jobWithId = job.copy(id = document.id)
            document.set(jobWithId)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobs(): Result<List<Job>> {
        return try {
            val currentUser =
                auth.currentUser ?: return Result.failure(Exception("User is not logged in"))
            val snapshot =
                jobsCollection().whereEqualTo("providerId", currentUser.uid).get().await()
            val jobs = snapshot.documents.mapNotNull { it.toObject(Job::class.java) }
            Result.success(jobs)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun deleteJob(
        jobId: String
    ): Result<Unit> {

        return try {

            jobsCollection()
                .document(jobId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getCustomerJobs(): Result<List<Job>> {
        return try {
            val currentUser =
                auth.currentUser ?: return Result.failure(Exception("User is not logged in"))
            val snapshot = jobsCollection()
                .whereEqualTo("customerId",currentUser.uid)
                .get()
                .await()
            val jobs = snapshot.documents.mapNotNull {
                it.toObject(Job::class.java)
            }
            Result.success(jobs)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}


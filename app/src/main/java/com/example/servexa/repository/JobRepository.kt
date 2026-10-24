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
                auth.currentUser
                    ?: return Result.failure(Exception("User is not logged in"))

            val snapshot =
                jobsCollection()
                    .whereEqualTo("providerId", currentUser.uid)
                    .get()
                    .await()

            val jobs = snapshot.documents
                .mapNotNull {
                    it.toObject(Job::class.java)
                }
                .filter { job ->
                    job.status != "Completed"
                }

            Result.success(jobs)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateJobStatus(
        jobId: String,
        status: String
    ): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            val updates = mutableMapOf<String, Any>("status" to status)

            if (status == "Accepted" && currentUser != null) {
                updates["providerId"] = currentUser.uid

                val userSnapshot = try {
                    firestore.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .await()
                } catch (_: Exception) {
                    null
                }

                val providerName = userSnapshot?.getString("name").orEmpty().ifBlank {
                    currentUser.displayName.orEmpty().ifBlank { "Provider" }
                }

                if (providerName.isNotBlank()) {
                    updates["providerName"] = providerName
                }
            }

            jobsCollection()
                .document(jobId)
                .update(updates)
                .await()

            Result.success(Unit)

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
                auth.currentUser
                    ?: return Result.failure(Exception("User is not logged in"))

            val snapshot = jobsCollection()
                .whereEqualTo("customerId", currentUser.uid)
                .get()
                .await()

            val jobs = snapshot.documents
                .mapNotNull {
                    it.toObject(Job::class.java)
                }
                .filter { job ->
                    job.status != "Completed"
                }

            Result.success(jobs)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markJobCompleted(
        jobId: String,
        isCustomer: Boolean
    ): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("User is not logged in"))

            val jobDocument = jobsCollection()
                .document(jobId)
                .get()
                .await()

            val job = jobDocument.toObject(Job::class.java)
                ?: return Result.failure(Exception("Job not found"))

            val customerCompleted =
                if (isCustomer) true else job.customerCompleted

            val providerCompleted =
                if (isCustomer) job.providerCompleted else true

            val newStatus =
                if (customerCompleted && providerCompleted) {
                    "Completed"
                } else {
                    job.status
                }

            jobsCollection()
                .document(jobId)
                .update(
                    mapOf(
                        if (isCustomer) {
                            "customerCompleted"
                        } else {
                            "providerCompleted"
                        } to true,

                        "status" to newStatus
                    )
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun approveProvider(jobId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("User is not logged in"))

            val jobDocument = jobsCollection()
                .document(jobId)
                .get()
                .await()

            val job = jobDocument.toObject(Job::class.java)
                ?: return Result.failure(Exception("Job not found"))

            // Only the customer who created this job can approve the provider.
            if (job.customerId != currentUser.uid) {
                return Result.failure(
                    Exception("Only the customer can approve this provider")
                )
            }

            // A provider must be assigned and must have accepted the job.
            if (job.providerId.isBlank() || job.status != "Accepted") {
                return Result.failure(
                    Exception("The provider must accept the job before approval")
                )
            }

            jobsCollection()
                .document(jobId)
                .update("customerApprovedProvider", true)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}


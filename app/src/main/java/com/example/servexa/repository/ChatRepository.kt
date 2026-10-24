
package com.example.servexa.repository

import android.util.Log
import com.example.servexa.model.ChatMessage
import com.example.servexa.model.Conversation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val conversationsCollection =
        firestore.collection("conversations")

    // Create a conversation or return the existing one.
//    suspend fun getOrCreateConversation(
//        otherUserId: String,
//        otherUserName: String,
//        jobId: String
//    ): Result<String> {
//        return try {
//            val currentUser = auth.currentUser
//                ?: return Result.failure(
//                    Exception("User is not logged in")
//                )
//
//            require(otherUserId != currentUser.uid) {
//                "You cannot start a conversation with yourself"
//            }
//
//            val conversationId = "${jobId}_${listOf(
//                currentUser.uid,
//                otherUserId
//            ).sorted().joinToString("_")}"
//
//            val conversationRef =
//                conversationsCollection.document(conversationId)
//
//            val existingConversation =
//                conversationRef.get().await()
//
//            if (!existingConversation.exists()) {
//                val conversation = Conversation(
//                    id = conversationId,
//                    participantIds = listOf(
//                        currentUser.uid,
//                        otherUserId
//                    ),
//                    participantNames = mapOf(
//                        currentUser.uid to
//                                (currentUser.displayName ?: "User"),
//                        otherUserId to otherUserName
//                    ),
//                    lastMessage = "",
//                    lastMessageAt = System.currentTimeMillis(),
//                    jobId = jobId
//                )
//
//                conversationRef.set(conversation).await()
//            }
//
//            Result.success(conversationId)
//
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }


    suspend fun getOrCreateConversation(
        otherUserId: String,
        otherUserName: String,
        jobId: String
    ): Result<String> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(
                    Exception("User is not logged in")
                )

            require(otherUserId != currentUser.uid) {
                "You cannot start a conversation with yourself"
            }

            // Fetch and validate the job document first
            val jobSnapshot = firestore
                .collection("jobs")
                .document(jobId)
                .get()
                .await()

            if (!jobSnapshot.exists()) {
                return Result.failure(Exception("Job not found"))
            }

            val customerId = jobSnapshot.getString("customerId") ?: ""
            val providerId = jobSnapshot.getString("providerId") ?: ""
            val status = jobSnapshot.getString("status") ?: ""

            if (currentUser.uid != customerId && currentUser.uid != providerId) {
                return Result.failure(
                    Exception("Current user is not a participant in this job")
                )
            }

            if (otherUserId != customerId && otherUserId != providerId) {
                return Result.failure(
                    Exception("Selected user is not a participant in this job")
                )
            }

            // Chat is permitted when the job has been accepted or completed
            if (status != "Accepted" && status != "Completed") {
                return Result.failure(
                    Exception("The job must be accepted before chat")
                )
            }

            // Deterministic conversation ID for both participants.
            val conversationId = "${jobId}_${listOf(
                customerId,
                providerId
            ).sorted().joinToString("_")}"

            val conversationRef = conversationsCollection.document(conversationId)

            // Check if conversation already exists
            val existingSnapshot = try {
                conversationRef.get().await()
            } catch (_: Exception) {
                null
            }

            if (existingSnapshot != null && existingSnapshot.exists()) {
                return Result.success(conversationId)
            }

            // Construct participant names map cleanly for both participants
            val rawCustomerName = jobSnapshot.getString("customerName").orEmpty()
            val rawProviderName = jobSnapshot.getString("providerName").orEmpty()

            val customerName = rawCustomerName.ifBlank {
                if (currentUser.uid == customerId) {
                    currentUser.displayName?.takeIf { it.isNotBlank() } ?: "Customer"
                } else {
                    "Customer"
                }
            }

            val providerName = rawProviderName.ifBlank {
                if (currentUser.uid == providerId) {
                    currentUser.displayName?.takeIf { it.isNotBlank() } ?: otherUserName
                } else {
                    otherUserName
                }
            }

            val conversation = Conversation(
                id = conversationId,
                participantIds = listOf(customerId, providerId),
                participantNames = mapOf(
                    customerId to customerName,
                    providerId to providerName
                ),
                lastMessage = "",
                lastMessageAt = System.currentTimeMillis(),
                jobId = jobId
            )

            conversationRef.set(conversation).await()

            Result.success(conversationId)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // Send a message and update the conversation preview.
    suspend fun sendMessage(
        conversationId: String,
        receiverId: String,
        text: String
    ): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(
                    Exception("User is not logged in")
                )

            val messageText = text.trim()

            if (messageText.isBlank()) {
                return Result.failure(
                    Exception("Message cannot be empty")
                )
            }

            val conversationRef =
                conversationsCollection.document(conversationId)

            val conversationSnapshot =
                conversationRef.get().await()

            val conversation =
                conversationSnapshot.toObject(Conversation::class.java)
                    ?: return Result.failure(
                        Exception("Conversation not found")
                    )

            val effectiveReceiverId = if (receiverId.isNotBlank() && receiverId != currentUser.uid) {
                receiverId
            } else {
                conversation.participantIds.firstOrNull { it != currentUser.uid }.orEmpty()
            }

            if (currentUser.uid !in conversation.participantIds ||
                effectiveReceiverId.isBlank() ||
                effectiveReceiverId !in conversation.participantIds
            ) {
                return Result.failure(
                    Exception("Invalid conversation participants")
                )
            }

            val messageRef =
                conversationRef.collection("messages").document()

            val timestamp = System.currentTimeMillis()

            val message = ChatMessage(
                id = messageRef.id,
                conversationId = conversationId,
                senderId = currentUser.uid,
                receiverId = effectiveReceiverId,
                text = messageText,
                sentAt = timestamp
            )

            firestore.runBatch { batch ->
                batch.set(messageRef, message)

                batch.update(
                    conversationRef,
                    mapOf(
                        "lastMessage" to messageText,
                        "lastMessageAt" to timestamp
                    )
                )
            }.await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Observe messages in real time.
    fun observeMessages(
        conversationId: String
    ): Flow<List<ChatMessage>> = callbackFlow {

        val currentUserId = auth.currentUser?.uid

        if (currentUserId == null) {
            close(Exception("User is not logged in"))
            return@callbackFlow
        }

        val conversationRef =
            conversationsCollection.document(conversationId)

        val conversationListener =
            conversationRef.addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e("ServexaChat", "conversationListener error: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val conversation =
                        snapshot.toObject(Conversation::class.java)

                    if (conversation != null &&
                        currentUserId !in conversation.participantIds
                    ) {
                        close(Exception("Access denied to conversation"))
                    }
                }
            }

        val messagesListener =
            conversationRef.collection("messages")
                .orderBy("sentAt", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        Log.e("ServexaChat", "messagesListener error: ${error.message}", error)
                        close(error)
                        return@addSnapshotListener
                    }

                    val messages = snapshot?.documents
                        ?.mapNotNull {
                            it.toObject(ChatMessage::class.java)
                        }
                        ?: emptyList()

                    trySend(messages)
                }

        awaitClose {
            conversationListener.remove()
            messagesListener.remove()
        }
    }

    // Observe the current user's conversation list in real time.
    fun observeConversations(): Flow<List<Conversation>> = callbackFlow {

        val currentUserId = auth.currentUser?.uid

        if (currentUserId == null) {
            close(Exception("User is not logged in"))
            return@callbackFlow
        }

        val listener = conversationsCollection
            .whereArrayContains("participantIds", currentUserId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e("ServexaChat", "observeConversations error: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }

                val conversations = snapshot?.documents
                    ?.mapNotNull {
                        it.toObject(Conversation::class.java)
                    }
                    ?.sortedByDescending { it.lastMessageAt }
                    ?: emptyList()

                trySend(conversations)
            }

        awaitClose {
            listener.remove()
        }
    }
}

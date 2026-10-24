package com.example.servexa.model

data class ChatMessage(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val sentAt: Long = 0L
)
package com.example.servexa.model
data class Conversation(
    val id: String = "",
    val participantIds: List<String> = emptyList(),
    val participantNames: Map<String, String> = emptyMap(),
    val lastMessage: String = "",
    val lastMessageAt: Long = 0L,
    val jobId: String = ""
)

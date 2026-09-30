package com.example.servexa.model


data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val currentRole: String = "",
    val role: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val services: List<String> = emptyList()
)
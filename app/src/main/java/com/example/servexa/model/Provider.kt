package com.example.servexa.model

data class Provider(
    val uid: String="",
    val name:String="",
    val email: String="",
    val services: List<String> = emptyList()
)
package com.example.servexa.model

data class Job(
    val id: String="",

    val title: String="",

    val customerId: String="",
    val customerName: String="",
    val providerId: String="",
    val providerName: String="",
    val description: String="",
    val status: String="Pending",
    val price: Double=0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val customerApprovedProvider: Boolean = false,
    val customerCompleted: Boolean = false,
    val providerCompleted: Boolean = false
)
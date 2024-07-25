package com.nolgaemi.servermonitorapp.model

data class ServerStatus(
    val id: Int, // Add this line
    val timestamp: String,
    val server_name: String,
    val server_ip: String,
    val network_status: String,
    val cpu_usage: Float,
    val memory_usage: Float,
    val disk_usage: Float
)

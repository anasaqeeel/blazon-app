package com.blazon.app.data.model

data class GroomingPackage(
    val id: String,
    val name: String,
    val price: Int,
    val description: String,
    val preWeddingServices: List<String>,
    val mehndiBaratWalimaServices: List<String>,
    val branchId: String
)

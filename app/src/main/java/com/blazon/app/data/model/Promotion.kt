package com.blazon.app.data.model

import java.util.Date

data class Promotion(
    val id: String,
    val branchId: String,
    val title: String,
    val description: String,
    val discount: Int,
    val validFrom: Date,
    val validTo: Date,
    val applicableServices: List<String>? = null
)


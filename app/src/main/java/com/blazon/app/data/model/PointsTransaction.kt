package com.blazon.app.data.model

import java.util.Date

data class PointsTransaction(
    val id: String,
    val userId: String,
    val type: TransactionType,
    val points: Int,
    val description: String,
    val serviceName: String? = null,
    val date: Date
)

enum class TransactionType {
    Earned,
    Redeemed,
    Bonus,
    Expired
}

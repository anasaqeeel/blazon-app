package com.blazon.app.data.model

import java.util.Date

data class User(
    val id: String,
    val name: String,
    val phone: String,
    val email: String? = null,
    val selectedBranchId: String,
    val totalVisits: Int,
    val loyaltyStatus: LoyaltyStatus,
    val discountEligible: Boolean,
    val discountUnlockedAt: Date? = null,
    val createdAt: Date,
    val totalPoints: Int = 0,
    val lifetimePoints: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)

enum class LoyaltyStatus {
    Bronze,
    Silver,
    Gold
}


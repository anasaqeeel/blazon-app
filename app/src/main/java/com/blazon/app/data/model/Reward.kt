package com.blazon.app.data.model

data class Reward(
    val id: String,
    val name: String,
    val description: String,
    val pointsCost: Int,
    val category: RewardCategory,
    val originalPrice: Int,
    val requiredTier: LoyaltyStatus = LoyaltyStatus.Bronze,
    val isLimitedTime: Boolean = false
)

enum class RewardCategory {
    FreeService,
    Discount,
    Upgrade,
    Exclusive
}

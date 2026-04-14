package com.blazon.app.data.model

data class LoyaltyInfo(
    val userId: String,
    val totalPoints: Int,
    val lifetimePoints: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val tier: LoyaltyStatus,
    val tierMultiplier: Double,
    val pointsToNextTier: Int,
    val nextTier: LoyaltyStatus?,
    val redeemedRewardsCount: Int
) {
    companion object {
        fun calculateTier(lifetimePoints: Int): LoyaltyStatus = when {
            lifetimePoints >= 5000 -> LoyaltyStatus.Gold
            lifetimePoints >= 1000 -> LoyaltyStatus.Silver
            else -> LoyaltyStatus.Bronze
        }

        fun getTierMultiplier(tier: LoyaltyStatus): Double = when (tier) {
            LoyaltyStatus.Bronze -> 1.0
            LoyaltyStatus.Silver -> 1.5
            LoyaltyStatus.Gold -> 2.0
        }

        fun getPointsToNextTier(lifetimePoints: Int): Int = when {
            lifetimePoints >= 5000 -> 0
            lifetimePoints >= 1000 -> 5000 - lifetimePoints
            else -> 1000 - lifetimePoints
        }

        fun getNextTier(currentTier: LoyaltyStatus): LoyaltyStatus? = when (currentTier) {
            LoyaltyStatus.Bronze -> LoyaltyStatus.Silver
            LoyaltyStatus.Silver -> LoyaltyStatus.Gold
            LoyaltyStatus.Gold -> null
        }

        fun getTierBenefits(tier: LoyaltyStatus): List<String> = when (tier) {
            LoyaltyStatus.Bronze -> listOf(
                "Earn 10% of price as points on every service",
                "Every 11th visit of any service is FREE",
                "Access to basic rewards catalog",
                "Birthday bonus points"
            )
            LoyaltyStatus.Silver -> listOf(
                "Earn 10% of price as points on every service",
                "Every 11th visit of any service is FREE",
                "Access to Silver-tier rewards catalog",
                "Priority booking",
                "Birthday bonus points (2x)"
            )
            LoyaltyStatus.Gold -> listOf(
                "Earn 10% of price as points on every service",
                "Every 11th visit of any service is FREE",
                "Access to ALL rewards including Gold exclusives",
                "VIP priority booking",
                "Birthday bonus points (3x)",
                "Complimentary beverages"
            )
        }
    }
}

data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val bonusPoints: Int,
    val progress: Int,
    val target: Int,
    val isCompleted: Boolean = progress >= target
)

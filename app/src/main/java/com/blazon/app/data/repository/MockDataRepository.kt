package com.blazon.app.data.repository

import com.blazon.app.data.model.*
import java.util.Date
import kotlinx.coroutines.delay

object MockDataRepository {

    val branches = listOf(
        Branch(
            id = "islamabad-1",
            name = "Blazon Islamabad - F-11",
            city = "Islamabad",
            address = "1st Floor, Bizzon Plaza, F-11 Markaz, Islamabad",
            phone = "0321-6331122",
            wifiNetwork = "Blazon_Islamabad_1",
            qrCode = "blazon_isb_1_qr",
            operatingHours = OperatingHours(open = "09:00", close = "22:00")
        ),
        Branch(
            id = "lahore-1",
            name = "Blazon Lahore - Mall Road",
            city = "Lahore",
            address = "Mall Road, Lahore",
            phone = "+92-300-1111111",
            wifiNetwork = "Blazon_Lahore_1",
            qrCode = "blazon_lhr_1_qr",
            operatingHours = OperatingHours(open = "09:00", close = "22:00")
        ),
        Branch(
            id = "lahore-2",
            name = "Blazon Lahore - DHA",
            city = "Lahore",
            address = "DHA Phase 5, Lahore",
            phone = "+92-300-2222222",
            wifiNetwork = "Blazon_Lahore_2",
            qrCode = "blazon_lhr_2_qr",
            operatingHours = OperatingHours(open = "10:00", close = "23:00")
        )
    )

    // Helper function to create services for all branches
    private fun createServicesForAllBranches(): List<Service> {
        val allBranchIds = branches.map { it.id }
        val serviceTemplates = listOf(
            // HAIR SERVICES (from handwritten menu)
            Service("s1", "Haircut", ServiceCategory.Hair, 1500, 45, "", "Professional haircut (30-45 mins)"),
            Service("s2", "Regular Styling", ServiceCategory.Hair, 700, 15, "", "Basic hair styling"),
            Service("s3", "Fiber Styling", ServiceCategory.Hair, 1000, 15, "", "Premium fiber-based styling"),
            Service("s4", "Hair-Do", ServiceCategory.Hair, 700, 30, "", "Complete hair styling service"),
            Service("s45", "Shampoo", ServiceCategory.Hair, 500, 15, "", "Hair shampoo service"),
            Service("s46", "Deep Condition", ServiceCategory.Hair, 800, 20, "", "Deep hair conditioning treatment"),
            Service("s47", "Hair Shine Boost", ServiceCategory.Hair, 2000, 40, "", "Hair shine enhancement"),

            // BEARD SERVICES (from handwritten menu)
            Service("s5", "Beard Reshaping", ServiceCategory.Beard, 800, 30, "", "Complete beard reshaping with cleansing & scrubbing"),
            Service("s50", "Beard Trim/Shave", ServiceCategory.Beard, 800, 25, "", "Professional beard trim or shave"),
            Service("s51", "Shave", ServiceCategory.Beard, 600, 20, "", "Clean shave service"),

            // COLORING SERVICES (from handwritten menu)
            Service("s6", "Hair Coloring", ServiceCategory.Coloring, 2500, 45, "", "Professional hair coloring"),
            Service("s7", "Keune Hair Color", ServiceCategory.Coloring, 700, 40, "", "Keune professional hair coloring"),
            Service("s8", "L'Oreal Hair Color", ServiceCategory.Coloring, 1000, 45, "", "L'Oreal premium hair coloring"),
            Service("s9", "Keune Full Color", ServiceCategory.Coloring, 2800, 60, "", "Complete Keune coloring service"),
            Service("s10", "Fashion Color", ServiceCategory.Coloring, 3000, 60, "", "Trendy fashion colors"),
            Service("s11", "Color Repair", ServiceCategory.Coloring, 9000, 90, "", "Professional color correction"),
            Service("s12", "L'Oreal Full Color", ServiceCategory.Coloring, 4750, 75, "", "Complete L'Oreal service"),
            Service("s52", "Hair Dye", ServiceCategory.Coloring, 1500, 45, "", "Professional hair dye service"),

            // TREATMENTS (from handwritten menu)
            Service("s13", "Protein Dose", ServiceCategory.Treatments, 3000, 45, "", "Hair protein treatment"),
            Service("s14", "Dandruff Control", ServiceCategory.Treatments, 1000, 30, "", "Anti-dandruff treatment"),
            Service("s15", "Shine Enhance", ServiceCategory.Treatments, 3000, 45, "", "Hair shine enhancement treatment"),
            Service("s16", "Moisture Reback", ServiceCategory.Treatments, 1000, 45, "", "Deep moisture restoration"),

            // TEXTURE SERVICES (from handwritten menu - price ranges)
            Service("s17", "Hair Keratin", ServiceCategory.Texture, 13000, 105, "", "Keratin smoothing treatment (Rs. 6,000 - 20,000)"),
            Service("s18", "Hair Botox", ServiceCategory.Texture, 14500, 105, "", "Hair botox treatment (Rs. 7,000 - 22,000)"),
            Service("s19", "Hair Perming", ServiceCategory.Texture, 19000, 180, "", "Professional hair perming"),

            // SKIN - FACIALS (from handwritten menu - all facials 60 mins)
            Service("s20", "Dermalogica Facial", ServiceCategory.Skin, 10000, 60, "", "Premium Dermalogica facial treatment"),
            Service("s21", "Thalgo Facial", ServiceCategory.Skin, 8500, 60, "", "Thalgo professional facial"),
            Service("s22", "Conatural Facial", ServiceCategory.Skin, 7000, 60, "", "Natural facial treatment"),
            Service("s23", "Janssen Facial", ServiceCategory.Skin, 6500, 60, "", "Janssen skincare facial"),
            Service("s24", "Skin Care Facial", ServiceCategory.Skin, 4500, 60, "", "Basic skincare facial"),
            Service("s25", "Combination Facial", ServiceCategory.Skin, 3000, 60, "", "Combination skin treatment"),
            Service("s53", "Janssen Whitening Facial", ServiceCategory.Skin, 7000, 60, "", "Janssen whitening facial treatment"),
            Service("s54", "Cleansing", ServiceCategory.Skin, 1500, 30, "", "Deep skin cleansing"),
            Service("s55", "Exfoliation", ServiceCategory.Skin, 2000, 35, "", "Skin exfoliation treatment"),
            Service("s56", "Face Mask", ServiceCategory.Skin, 1500, 25, "", "Face mask treatment"),
            Service("s57", "Touch-Ups", ServiceCategory.Skin, 1000, 20, "", "Skin touch-up service"),
            Service("s58", "Intensive Cleansing", ServiceCategory.Skin, 2500, 45, "", "Intensive deep cleansing"),
            Service("s59", "Skin Toning", ServiceCategory.Skin, 1500, 30, "", "Skin toning treatment"),
            Service("s60", "Cleansing & Toning", ServiceCategory.Skin, 2000, 35, "", "Combined cleansing and toning"),
            Service("s61", "Multi Active Toning", ServiceCategory.Skin, 2000, 35, "", "Multi-active skin toning"),
            Service("s62", "Oil Control Treatment", ServiceCategory.Skin, 2000, 40, "", "Oil control facial treatment"),
            Service("s63", "Brightening Mask", ServiceCategory.Skin, 1800, 30, "", "Skin brightening mask"),

            // POLISHER
            Service("s26", "Face Polisher", ServiceCategory.Polisher, 1500, 30, "", "Face polishing service"),
            Service("s27", "Arms Polisher", ServiceCategory.Polisher, 1500, 30, "", "Arms polishing service"),
            Service("s28", "Hands Polisher", ServiceCategory.Polisher, 1000, 25, "", "Hands polishing service"),
            Service("s29", "Feet Polisher", ServiceCategory.Polisher, 1000, 25, "", "Feet polishing service"),
            Service("s30", "Neck Polisher", ServiceCategory.Polisher, 800, 20, "", "Neck polishing service"),
            Service("s31", "Black Mask", ServiceCategory.Polisher, 600, 20, "", "Black mask treatment"),
            Service("s32", "Mud Mask", ServiceCategory.Polisher, 500, 15, "", "Mud mask treatment"),
            Service("s64", "Whiten Face Polisher", ServiceCategory.Polisher, 1800, 35, "", "Whitening face polisher"),
            Service("s65", "Full Hands and Neck Polish", ServiceCategory.Polisher, 2500, 50, "", "Complete hands and neck polishing"),
            Service("s66", "Black/White Heads Removal", ServiceCategory.Polisher, 2000, 40, "", "Black and white heads removal"),
            Service("s67", "Microfoliant Exfoliation", ServiceCategory.Polisher, 2200, 45, "", "Microfoliant exfoliation treatment"),

            // MASSAGE
            Service("s33", "Head Massage", ServiceCategory.Massage, 700, 30, "", "Relaxing head massage"),
            Service("s34", "Shoulders Massage", ServiceCategory.Massage, 500, 25, "", "Shoulder relief massage"),
            Service("s35", "Back Massage", ServiceCategory.Massage, 600, 30, "", "Back massage therapy"),
            Service("s36", "Full Upper Massage", ServiceCategory.Massage, 1800, 60, "", "Complete upper body massage"),

            // NAIL CARE
            Service("s37", "Manicure/Pedicure", ServiceCategory.Nails, 5000, 90, "", "Complete nail care service"),
            Service("s68", "Whitening Manicure", ServiceCategory.Nails, 3000, 60, "", "Whitening manicure service"),
            Service("s69", "Whitening Manicure & Pedicure", ServiceCategory.Nails, 5500, 120, "", "Complete whitening nail care"),

            // WAX
            Service("s38", "Full Face Wax", ServiceCategory.Wax, 1500, 30, "", "Complete face waxing"),
            Service("s39", "Cheeks Wax", ServiceCategory.Wax, 500, 15, "", "Cheeks waxing"),
            Service("s40", "Nose Wax", ServiceCategory.Wax, 500, 10, "", "Nose waxing"),
            Service("s41", "Ear Wax", ServiceCategory.Wax, 500, 10, "", "Ear waxing"),

            // GROOMING (from handwritten menu)
            Service("s42", "Party Grooming", ServiceCategory.Grooming, 5000, 90, "", "Complete party grooming package"),
            Service("s43", "Special Effects", ServiceCategory.Grooming, 3500, 75, "", "Special effects makeup"),
            Service("s44", "Character Make-up", ServiceCategory.Grooming, 3750, 80, "", "Character makeup service")
        )

        // Create services for all branches
        return allBranchIds.flatMap { branchId ->
            serviceTemplates.mapIndexed { index, template ->
                Service(
                    id = "${template.id}-$branchId",
                    name = template.name,
                    category = template.category,
                    price = template.price,
                    duration = template.duration,
                    branchId = branchId,
                    description = template.description
                )
            }
        }
    }

    val services = createServicesForAllBranches()

    var mockUser = User(
        id = "user-1",
        name = "Ahmed Khan",
        phone = "+92-300-9999999",
        email = "ahmed@example.com",
        selectedBranchId = "islamabad-1",
        totalVisits = 8,
        loyaltyStatus = LoyaltyStatus.Silver,
        discountEligible = false,
        createdAt = Date(1705276800000), // 2024-01-15
        totalPoints = 1850,
        lifetimePoints = 2350,
        currentStreak = 3,
        longestStreak = 7
    )

    val mockBarberAvailability = mapOf(
        "islamabad-1" to BarberAvailability("islamabad-1", 5, 12, Date()),
        "lahore-1" to BarberAvailability("lahore-1", 5, 12, Date()),
        "lahore-2" to BarberAvailability("lahore-2", 3, 7, Date())
    )

    val mockPromotions = listOf(
        Promotion(
            id = "promo-1",
            branchId = "islamabad-1",
            title = "Grand Opening Special",
            description = "30% off all services this month",
            discount = 30,
            validFrom = Date(1704067200000), // 2025-01-01
            validTo = Date(1706659200000) // 2025-01-31
        ),
        Promotion(
            id = "promo-2",
            branchId = "islamabad-1",
            title = "Double Points Week",
            description = "Earn 2x points on all services!",
            discount = 0,
            validFrom = Date(1704067200000),
            validTo = Date(1709251200000)
        ),
        Promotion(
            id = "promo-3",
            branchId = "lahore-1",
            title = "Grand Opening Special",
            description = "30% off all services this month",
            discount = 30,
            validFrom = Date(1704067200000),
            validTo = Date(1706659200000)
        ),
        Promotion(
            id = "promo-4",
            branchId = "lahore-2",
            title = "Grand Opening Special",
            description = "30% off all services this month",
            discount = 30,
            validFrom = Date(1704067200000),
            validTo = Date(1706659200000)
        )
    )

    // ==================== REWARDS CATALOG ====================
    val rewardsCatalog = listOf(
        Reward(
            id = "rwd-1",
            name = "Free Shave",
            description = "Redeem for a complimentary clean shave service",
            pointsCost = 300,
            category = RewardCategory.FreeService,
            originalPrice = 600,
            requiredTier = LoyaltyStatus.Bronze
        ),
        Reward(
            id = "rwd-2",
            name = "Free Head Massage",
            description = "Redeem for a relaxing 30-min head massage",
            pointsCost = 400,
            category = RewardCategory.FreeService,
            originalPrice = 700,
            requiredTier = LoyaltyStatus.Bronze
        ),
        Reward(
            id = "rwd-3",
            name = "Free Shampoo + Condition",
            description = "Shampoo and deep conditioning combo on the house",
            pointsCost = 650,
            category = RewardCategory.FreeService,
            originalPrice = 1300,
            requiredTier = LoyaltyStatus.Bronze
        ),
        Reward(
            id = "rwd-4",
            name = "Free Haircut",
            description = "Redeem for a professional haircut worth Rs. 1,500",
            pointsCost = 800,
            category = RewardCategory.FreeService,
            originalPrice = 1500,
            requiredTier = LoyaltyStatus.Bronze
        ),
        Reward(
            id = "rwd-5",
            name = "Free Beard Reshaping",
            description = "Complete beard reshaping with cleansing & scrubbing",
            pointsCost = 450,
            category = RewardCategory.FreeService,
            originalPrice = 800,
            requiredTier = LoyaltyStatus.Bronze
        ),
        Reward(
            id = "rwd-6",
            name = "20% Off Any Service",
            description = "Get 20% discount on any single service of your choice",
            pointsCost = 500,
            category = RewardCategory.Discount,
            originalPrice = 0,
            requiredTier = LoyaltyStatus.Bronze
        ),
        Reward(
            id = "rwd-7",
            name = "Free Full Upper Massage",
            description = "60-minute full upper body massage, totally free",
            pointsCost = 1000,
            category = RewardCategory.FreeService,
            originalPrice = 1800,
            requiredTier = LoyaltyStatus.Silver
        ),
        Reward(
            id = "rwd-8",
            name = "Free Skin Care Facial",
            description = "Complete 60-minute skincare facial session",
            pointsCost = 1200,
            category = RewardCategory.FreeService,
            originalPrice = 4500,
            requiredTier = LoyaltyStatus.Silver
        ),
        Reward(
            id = "rwd-9",
            name = "Free Manicure/Pedicure",
            description = "Complete nail care service worth Rs. 5,000",
            pointsCost = 1500,
            category = RewardCategory.FreeService,
            originalPrice = 5000,
            requiredTier = LoyaltyStatus.Silver
        ),
        Reward(
            id = "rwd-10",
            name = "50% Off Any Service",
            description = "Half price on ANY service - no limits!",
            pointsCost = 1800,
            category = RewardCategory.Discount,
            originalPrice = 0,
            requiredTier = LoyaltyStatus.Silver
        ),
        Reward(
            id = "rwd-11",
            name = "Free Thalgo Facial",
            description = "Premium Thalgo facial worth Rs. 8,500 - on us",
            pointsCost = 2500,
            category = RewardCategory.FreeService,
            originalPrice = 8500,
            requiredTier = LoyaltyStatus.Gold
        ),
        Reward(
            id = "rwd-12",
            name = "Free Dermalogica Facial",
            description = "Our best facial - Dermalogica premium treatment",
            pointsCost = 3000,
            category = RewardCategory.FreeService,
            originalPrice = 10000,
            requiredTier = LoyaltyStatus.Gold
        ),
        Reward(
            id = "rwd-13",
            name = "Free Hair Keratin",
            description = "Keratin smoothing treatment - the ultimate reward",
            pointsCost = 4500,
            category = RewardCategory.FreeService,
            originalPrice = 13000,
            requiredTier = LoyaltyStatus.Gold
        ),
        Reward(
            id = "rwd-14",
            name = "Free Party Grooming",
            description = "Complete party grooming package worth Rs. 5,000",
            pointsCost = 2000,
            category = RewardCategory.Exclusive,
            originalPrice = 5000,
            requiredTier = LoyaltyStatus.Gold
        ),
        Reward(
            id = "rwd-15",
            name = "Service Upgrade",
            description = "Upgrade any service to its premium variant for free",
            pointsCost = 600,
            category = RewardCategory.Upgrade,
            originalPrice = 0,
            requiredTier = LoyaltyStatus.Silver,
            isLimitedTime = true
        )
    )

    // ==================== POINTS TRANSACTIONS (History) ====================
    val mockTransactions = mutableListOf(
        PointsTransaction(
            id = "txn-1",
            userId = "user-1",
            type = TransactionType.Earned,
            points = 150,
            description = "Haircut at Blazon Islamabad",
            serviceName = "Haircut",
            date = Date(System.currentTimeMillis() - 86400000L * 30) // 30 days ago
        ),
        PointsTransaction(
            id = "txn-2",
            userId = "user-1",
            type = TransactionType.Earned,
            points = 80,
            description = "Beard Reshaping at Blazon Islamabad",
            serviceName = "Beard Reshaping",
            date = Date(System.currentTimeMillis() - 86400000L * 25)
        ),
        PointsTransaction(
            id = "txn-3",
            userId = "user-1",
            type = TransactionType.Bonus,
            points = 200,
            description = "3-visit streak bonus!",
            date = Date(System.currentTimeMillis() - 86400000L * 25)
        ),
        PointsTransaction(
            id = "txn-4",
            userId = "user-1",
            type = TransactionType.Earned,
            points = 250,
            description = "Hair Coloring at Blazon Islamabad",
            serviceName = "Hair Coloring",
            date = Date(System.currentTimeMillis() - 86400000L * 20)
        ),
        PointsTransaction(
            id = "txn-5",
            userId = "user-1",
            type = TransactionType.Redeemed,
            points = -400,
            description = "Redeemed: Free Head Massage",
            serviceName = "Head Massage",
            date = Date(System.currentTimeMillis() - 86400000L * 18)
        ),
        PointsTransaction(
            id = "txn-6",
            userId = "user-1",
            type = TransactionType.Earned,
            points = 300,
            description = "Protein Dose at Blazon Islamabad",
            serviceName = "Protein Dose",
            date = Date(System.currentTimeMillis() - 86400000L * 14)
        ),
        PointsTransaction(
            id = "txn-7",
            userId = "user-1",
            type = TransactionType.Bonus,
            points = 500,
            description = "Silver tier welcome bonus!",
            date = Date(System.currentTimeMillis() - 86400000L * 14)
        ),
        PointsTransaction(
            id = "txn-8",
            userId = "user-1",
            type = TransactionType.Earned,
            points = 225,
            description = "Haircut + Styling at Blazon Islamabad",
            serviceName = "Haircut",
            date = Date(System.currentTimeMillis() - 86400000L * 7)
        ),
        PointsTransaction(
            id = "txn-9",
            userId = "user-1",
            type = TransactionType.Redeemed,
            points = -300,
            description = "Redeemed: Free Shave",
            serviceName = "Shave",
            date = Date(System.currentTimeMillis() - 86400000L * 5)
        ),
        PointsTransaction(
            id = "txn-10",
            userId = "user-1",
            type = TransactionType.Earned,
            points = 345,
            description = "Janssen Facial at Blazon Islamabad (1.5x Silver)",
            serviceName = "Janssen Facial",
            date = Date(System.currentTimeMillis() - 86400000L * 2)
        ),
        PointsTransaction(
            id = "txn-11",
            userId = "user-1",
            type = TransactionType.Bonus,
            points = 100,
            description = "Weekly check-in bonus",
            date = Date(System.currentTimeMillis() - 86400000L)
        )
    )

    // ==================== CHALLENGES ====================
    val mockChallenges = listOf(
        Challenge(
            id = "ch-1",
            title = "First Timer",
            description = "Try 3 different service categories",
            bonusPoints = 300,
            progress = 2,
            target = 3
        ),
        Challenge(
            id = "ch-2",
            title = "Streak Master",
            description = "Visit 5 times in a row (weekly)",
            bonusPoints = 500,
            progress = 3,
            target = 5
        ),
        Challenge(
            id = "ch-3",
            title = "Skin Care Pro",
            description = "Get 3 facial treatments",
            bonusPoints = 400,
            progress = 1,
            target = 3
        ),
        Challenge(
            id = "ch-4",
            title = "Big Spender",
            description = "Spend Rs. 10,000 in a single visit",
            bonusPoints = 750,
            progress = 0,
            target = 1
        ),
        Challenge(
            id = "ch-5",
            title = "Loyal Regular",
            description = "Complete 10 total visits",
            bonusPoints = 1000,
            progress = 8,
            target = 10
        ),
        Challenge(
            id = "ch-6",
            title = "Full Package",
            description = "Try services from all 11 categories",
            bonusPoints = 1500,
            progress = 4,
            target = 11
        )
    )

    suspend fun getBranches(): List<Branch> {
        delay(500) // Simulate network delay
        return branches
    }

    suspend fun getBranchById(id: String): Branch? {
        delay(300)
        return branches.find { it.id == id }
    }

    suspend fun getServicesByBranch(branchId: String): List<Service> {
        delay(400)
        return services.filter { it.branchId == branchId }
    }

    suspend fun getUserData(userId: String): User? {
        delay(300)
        return if (userId == mockUser.id) mockUser else null
    }

    suspend fun getBarberAvailability(branchId: String): BarberAvailability? {
        delay(300)
        return mockBarberAvailability[branchId]
    }

    suspend fun getPromotionsByBranch(branchId: String): List<Promotion> {
        delay(300)
        return mockPromotions.filter { it.branchId == branchId }
    }

    // ==================== POINTS & REWARDS FUNCTIONS ====================

    suspend fun getRewardsCatalog(userTier: LoyaltyStatus): List<Reward> {
        delay(300)
        return rewardsCatalog.filter { reward ->
            when (userTier) {
                LoyaltyStatus.Gold -> true
                LoyaltyStatus.Silver -> reward.requiredTier != LoyaltyStatus.Gold
                LoyaltyStatus.Bronze -> reward.requiredTier == LoyaltyStatus.Bronze
            }
        }
    }

    suspend fun getAllRewards(): List<Reward> {
        delay(300)
        return rewardsCatalog
    }

    suspend fun getPointsHistory(userId: String): List<PointsTransaction> {
        delay(300)
        return mockTransactions
            .filter { it.userId == userId }
            .sortedByDescending { it.date }
    }

    suspend fun getLoyaltyInfo(userId: String): LoyaltyInfo? {
        delay(300)
        val user = if (userId == mockUser.id) mockUser else return null
        val tier = LoyaltyInfo.calculateTier(user.lifetimePoints)
        return LoyaltyInfo(
            userId = user.id,
            totalPoints = user.totalPoints,
            lifetimePoints = user.lifetimePoints,
            currentStreak = user.currentStreak,
            longestStreak = user.longestStreak,
            tier = tier,
            tierMultiplier = LoyaltyInfo.getTierMultiplier(tier),
            pointsToNextTier = LoyaltyInfo.getPointsToNextTier(user.lifetimePoints),
            nextTier = LoyaltyInfo.getNextTier(tier),
            redeemedRewardsCount = mockTransactions.count {
                it.userId == userId && it.type == TransactionType.Redeemed
            }
        )
    }

    suspend fun getChallenges(userId: String): List<Challenge> {
        delay(300)
        return mockChallenges
    }

    suspend fun redeemReward(userId: String, rewardId: String): RedeemResult {
        delay(500)
        val reward = rewardsCatalog.find { it.id == rewardId }
            ?: return RedeemResult(false, "Reward not found", 0)

        if (mockUser.totalPoints < reward.pointsCost) {
            return RedeemResult(false, "Not enough points. You need ${reward.pointsCost - mockUser.totalPoints} more points.", 0)
        }

        // Deduct points
        mockUser = mockUser.copy(totalPoints = mockUser.totalPoints - reward.pointsCost)

        // Add transaction
        mockTransactions.add(
            PointsTransaction(
                id = "txn-${System.currentTimeMillis()}",
                userId = userId,
                type = TransactionType.Redeemed,
                points = -reward.pointsCost,
                description = "Redeemed: ${reward.name}",
                serviceName = reward.name,
                date = Date()
            )
        )

        return RedeemResult(
            success = true,
            message = "Successfully redeemed ${reward.name}! Show this to your barber.",
            remainingPoints = mockUser.totalPoints
        )
    }

    // ==================== PER-SERVICE PROGRESS (Every 11th Free) ====================
    // Key: (userId, serviceName) → keeps count stable across branches
    private val serviceVisitCounters = mutableMapOf<Pair<String, String>, Int>(
        "user-1" to "Haircut" to 7,
        "user-1" to "Beard Reshaping" to 3,
        "user-1" to "Shave" to 10,           // ← next Shave is FREE
        "user-1" to "Hair Coloring" to 2,
        "user-1" to "Head Massage" to 5,
        "user-1" to "Janssen Facial" to 1
    )
    private val freeRedeemCounters = mutableMapOf<Pair<String, String>, Int>(
        "user-1" to "Hair Coloring" to 1     // customer has already claimed 1 free Hair Coloring
    )

    private fun serviceTemplateByName(name: String) =
        services.firstOrNull { it.name == name }

    suspend fun getServiceProgress(userId: String): List<ServiceProgress> {
        delay(200)
        val keys = (serviceVisitCounters.keys + freeRedeemCounters.keys)
            .filter { it.first == userId }
            .distinct()
        return keys.mapNotNull { key ->
            val template = serviceTemplateByName(key.second) ?: return@mapNotNull null
            val paid = serviceVisitCounters[key] ?: 0
            val freeCount = freeRedeemCounters[key] ?: 0
            ServiceProgress(
                serviceName = template.name,
                category = template.category,
                price = template.price,
                paidVisits = paid,
                freeRedeemed = freeCount,
                isFreeReady = paid >= 10
            )
        }.sortedWith(
            compareByDescending<ServiceProgress> { it.isFreeReady }
                .thenByDescending { it.paidVisits }
        )
    }

    // Earn points for a specific service at 10% flat of its price.
    // If the user has 10 paid visits of that service, THIS visit is FREE
    // (no charge, no base points, counter resets).
    suspend fun earnPointsForService(userId: String, serviceId: String): EarnResult {
        delay(300)
        val service = services.find { it.id == serviceId }
            ?: return EarnResult(0, 0, 0, mockUser.totalPoints, mockUser.currentStreak, false, false, "", 0, 0)

        val key = userId to service.name
        val currentCount = serviceVisitCounters[key] ?: 0
        val isFreeRedemption = currentCount >= 10

        // Flat 10% of service price
        val earnedPoints = if (isFreeRedemption) 0 else service.price / 10

        // Streak tracking (independent of free/paid)
        val newStreak = mockUser.currentStreak + 1
        var streakBonus = 0
        if (newStreak % 3 == 0) streakBonus += 200
        if (newStreak % 5 == 0) streakBonus += 500

        val totalEarned = earnedPoints + streakBonus

        // Update per-service counter
        if (isFreeRedemption) {
            serviceVisitCounters[key] = 0
            freeRedeemCounters[key] = (freeRedeemCounters[key] ?: 0) + 1
        } else {
            serviceVisitCounters[key] = currentCount + 1
        }

        val newPaidCount = serviceVisitCounters[key] ?: 0

        val tierBefore = mockUser.loyaltyStatus
        mockUser = mockUser.copy(
            totalPoints = mockUser.totalPoints + totalEarned,
            lifetimePoints = mockUser.lifetimePoints + totalEarned,
            totalVisits = mockUser.totalVisits + 1,
            currentStreak = newStreak,
            longestStreak = maxOf(mockUser.longestStreak, newStreak),
            loyaltyStatus = LoyaltyInfo.calculateTier(mockUser.lifetimePoints + totalEarned)
        )
        val tierUp = tierBefore != mockUser.loyaltyStatus

        // Transactions
        if (isFreeRedemption) {
            mockTransactions.add(
                PointsTransaction(
                    id = "txn-${System.currentTimeMillis()}",
                    userId = userId,
                    type = TransactionType.Bonus,
                    points = 0,
                    description = "FREE ${service.name} — 11th visit reward (Rs. ${service.price} value)",
                    serviceName = service.name,
                    date = Date()
                )
            )
        } else {
            mockTransactions.add(
                PointsTransaction(
                    id = "txn-${System.currentTimeMillis()}",
                    userId = userId,
                    type = TransactionType.Earned,
                    points = earnedPoints,
                    description = "${service.name} (Rs. ${service.price}) — 10% back",
                    serviceName = service.name,
                    date = Date()
                )
            )
        }
        if (streakBonus > 0) {
            mockTransactions.add(
                PointsTransaction(
                    id = "txn-${System.currentTimeMillis() + 1}",
                    userId = userId,
                    type = TransactionType.Bonus,
                    points = streakBonus,
                    description = "${newStreak}-visit streak bonus!",
                    date = Date()
                )
            )
        }

        return EarnResult(
            pointsEarned = earnedPoints,
            streakBonus = streakBonus,
            totalEarned = totalEarned,
            newBalance = mockUser.totalPoints,
            newStreak = newStreak,
            tierUp = tierUp,
            isFreeService = isFreeRedemption,
            serviceName = service.name,
            servicePrice = service.price,
            newPaidVisitCount = newPaidCount
        )
    }

    // Grooming Packages
    val groomingPackages = listOf(
        GroomingPackage(
            id = "pkg-1",
            name = "Grooming Package",
            price = 22000,
            description = "Complete grooming package for pre-wedding and wedding events",
            preWeddingServices = listOf(
                "Skin Care Facial",
                "Black/White Heads Removal",
                "Hair Shine Boost/Hair Dye",
                "Microfoliant Exfoliation",
                "Whitening Manicure",
                "Intensive Cleansing",
                "Beard Trim/Shave",
                "Face Polisher",
                "Skin Toning",
                "Black Mask",
                "Hair Cut"
            ),
            mehndiBaratWalimaServices = listOf(
                "Cleansing",
                "Exfoliation",
                "Face Mask",
                "Touch-Ups",
                "Shave",
                "Shampoo",
                "Deep Condition",
                "Hair-Do"
            ),
            branchId = "islamabad-1"
        ),
        GroomingPackage(
            id = "pkg-2",
            name = "Premium Grooming Package",
            price = 24000,
            description = "Premium package with Janssen whitening facial",
            preWeddingServices = listOf(
                "Janssen Whitening Facial",
                "Black/White Heads Removal",
                "Hair Shine Boost/Hair Dye",
                "Microfoliant Exfoliation",
                "Whitening Manicure",
                "Intensive Cleansing",
                "Beard Trim/Shave",
                "Face Polisher",
                "Skin Toning",
                "Black Mask",
                "Hair Cut"
            ),
            mehndiBaratWalimaServices = listOf(
                "Cleansing",
                "Exfoliation",
                "Face Mask",
                "Touch-Ups",
                "Shave",
                "Shampoo",
                "Deep Condition",
                "Hair-Do"
            ),
            branchId = "islamabad-1"
        ),
        GroomingPackage(
            id = "pkg-3",
            name = "Premium Plus Grooming Package",
            price = 26000,
            description = "Premium plus with Thalgo facial and enhanced services",
            preWeddingServices = listOf(
                "Thalgo Facial",
                "Whitening Manicure & Pedicure",
                "Full Hands and Neck Polish",
                "Black/White Heads Removal",
                "Hair Shine Enhance/Hair Dye",
                "Whiten Face Polisher",
                "Oil Control Treatment",
                "Cleansing & Toning",
                "Brightening Mask",
                "Beard Trim/Shave",
                "Hair Cut"
            ),
            mehndiBaratWalimaServices = listOf(
                "Cleansing",
                "Exfoliation",
                "Face Mask",
                "Touch-Ups",
                "Shave",
                "Shampoo",
                "Deep Condition",
                "Hair-Do"
            ),
            branchId = "islamabad-1"
        ),
        GroomingPackage(
            id = "pkg-4",
            name = "Signature Grooming Package",
            price = 33000,
            description = "Ultimate signature package with Dermalogica facial",
            preWeddingServices = listOf(
                "Dermalogica Facial",
                "Whitening Manicure & Pedicure",
                "Full Hands and Neck Polish",
                "Black/White Heads Removal",
                "Hair Shine Enhance/Hair Dye",
                "Whiten Face Polisher",
                "Cleansing & Exfoliation",
                "Multi Active Toning",
                "Brightening Mask",
                "Beard Trim/Shave",
                "Hair Cut"
            ),
            mehndiBaratWalimaServices = listOf(
                "Cleansing",
                "Exfoliation",
                "Face Mask",
                "Touch-Ups",
                "Shave",
                "Shampoo",
                "Deep Condition",
                "Hair-Do"
            ),
            branchId = "islamabad-1"
        ),
        GroomingPackage(
            id = "pkg-5",
            name = "Wedding Day Services",
            price = 5000,
            description = "Essential services for your wedding day",
            preWeddingServices = emptyList(),
            mehndiBaratWalimaServices = listOf(
                "Cleansing",
                "Exfoliation",
                "Face Mask",
                "Touch-Ups",
                "Shave",
                "Shampoo",
                "Deep Condition",
                "Hair-Do"
            ),
            branchId = "islamabad-1"
        )
    )

    suspend fun getGroomingPackages(branchId: String): List<GroomingPackage> {
        delay(300)
        return groomingPackages.filter { it.branchId == branchId }
    }
}

data class RedeemResult(
    val success: Boolean,
    val message: String,
    val remainingPoints: Int
)

data class EarnResult(
    val pointsEarned: Int,
    val streakBonus: Int,
    val totalEarned: Int,
    val newBalance: Int,
    val newStreak: Int,
    val tierUp: Boolean,
    val isFreeService: Boolean = false,
    val serviceName: String = "",
    val servicePrice: Int = 0,
    val newPaidVisitCount: Int = 0
)

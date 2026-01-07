package com.blazon.app.data.repository

import com.blazon.app.data.model.*
import java.util.Date
import kotlinx.coroutines.delay

object MockDataRepository {
    
    val branches = listOf(
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
        ),
        Branch(
            id = "islamabad-1",
            name = "Blazon Islamabad - F-7",
            city = "Islamabad",
            address = "F-7 Markaz, Islamabad",
            phone = "+92-300-3333333",
            wifiNetwork = "Blazon_Islamabad_1",
            qrCode = "blazon_isb_1_qr",
            operatingHours = OperatingHours(open = "09:30", close = "21:30")
        )
    )
    
    val services = listOf(
        Service("s1", "Classic Haircut", ServiceCategory.Hair, 1500, 30, "lahore-1", "Timeless cut with precision"),
        Service("s2", "Premium Haircut", ServiceCategory.Hair, 2500, 45, "lahore-1", "Expert styling with consultation"),
        Service("s3", "Fade/Undercut", ServiceCategory.Hair, 2000, 40, "lahore-1"),
        Service("s4", "Hair Styling", ServiceCategory.Hair, 1200, 20, "lahore-1"),
        Service("s5", "Beard Trim", ServiceCategory.Beard, 800, 20, "lahore-1"),
        Service("s6", "Beard Shaping", ServiceCategory.Beard, 1200, 25, "lahore-1"),
        Service("s7", "Beard Oil Treatment", ServiceCategory.Beard, 600, 15, "lahore-1"),
        Service("s8", "Facial Cleaning", ServiceCategory.Skin, 2000, 45, "lahore-1"),
        Service("s9", "Deep Cleansing", ServiceCategory.Skin, 2500, 50, "lahore-1"),
        Service("s10", "Anti-Aging Treatment", ServiceCategory.Skin, 3500, 60, "lahore-1"),
        Service("s11", "Head Massage", ServiceCategory.Massage, 1000, 30, "lahore-1"),
        Service("s12", "Full Body Massage", ServiceCategory.Massage, 3000, 60, "lahore-1"),
        Service("s13", "Neck & Shoulder Relief", ServiceCategory.Massage, 1500, 40, "lahore-1")
    )
    
    val mockUser = User(
        id = "user-1",
        name = "Ahmed Khan",
        phone = "+92-300-9999999",
        email = "ahmed@example.com",
        selectedBranchId = "lahore-1",
        totalVisits = 8,
        loyaltyStatus = LoyaltyStatus.Bronze,
        discountEligible = false,
        createdAt = Date(1705276800000) // 2024-01-15
    )
    
    val mockBarberAvailability = mapOf(
        "lahore-1" to BarberAvailability("lahore-1", 5, 12, Date()),
        "lahore-2" to BarberAvailability("lahore-2", 3, 7, Date()),
        "islamabad-1" to BarberAvailability("islamabad-1", 4, 10, Date())
    )
    
    val mockPromotions = listOf(
        Promotion(
            id = "promo-1",
            branchId = "lahore-1",
            title = "Grand Opening Special",
            description = "30% off all services this month",
            discount = 30,
            validFrom = Date(1704067200000), // 2025-01-01
            validTo = Date(1706659200000) // 2025-01-31
        ),
        Promotion(
            id = "promo-2",
            branchId = "lahore-1",
            title = "New Year Membership",
            description = "Get a free service with membership",
            discount = 15,
            validFrom = Date(1704067200000), // 2025-01-01
            validTo = Date(1709251200000) // 2025-02-28
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
}


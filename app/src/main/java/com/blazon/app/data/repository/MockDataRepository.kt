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
            // HAIR SERVICES
            Service("s1", "Regular Haircut (Wash & Styling)", ServiceCategory.Hair, 1500, 45, "", "Professional haircut with wash and styling"),
            Service("s2", "Regular Style", ServiceCategory.Hair, 500, 20, "", "Basic hair styling"),
            Service("s3", "Fiber Styling", ServiceCategory.Hair, 700, 25, "", "Premium fiber-based styling"),
            Service("s4", "Hair-Do", ServiceCategory.Hair, 700, 30, "", "Complete hair styling service"),
            Service("s45", "Hair Cut", ServiceCategory.Hair, 1500, 30, "", "Professional haircut"),
            Service("s46", "Shampoo", ServiceCategory.Hair, 500, 15, "", "Hair shampoo service"),
            Service("s47", "Deep Condition", ServiceCategory.Hair, 800, 20, "", "Deep hair conditioning treatment"),
            Service("s48", "Hair Shine Boost", ServiceCategory.Hair, 2000, 40, "", "Hair shine enhancement"),
            Service("s49", "Hair Shine Enhance", ServiceCategory.Hair, 2000, 40, "", "Premium hair shine treatment"),
            
            // BEARD SERVICES
            Service("s5", "Beard Reshaping (Cleansing & Scrubbing)", ServiceCategory.Beard, 800, 30, "", "Complete beard grooming with cleansing"),
            Service("s50", "Beard Trim/Shave", ServiceCategory.Beard, 800, 25, "", "Professional beard trim or shave"),
            Service("s51", "Shave", ServiceCategory.Beard, 600, 20, "", "Clean shave service"),
            
            // COLORING SERVICES
            Service("s6", "Keune Hair Color", ServiceCategory.Coloring, 700, 40, "", "Keune professional hair coloring"),
            Service("s7", "L'Oreal Hair Color", ServiceCategory.Coloring, 1000, 45, "", "L'Oreal premium hair coloring"),
            Service("s8", "Keune Full Color", ServiceCategory.Coloring, 2800, 60, "", "Complete Keune coloring service"),
            Service("s9", "Fashion Color", ServiceCategory.Coloring, 3000, 60, "", "Trendy fashion colors (2500-3500)"),
            Service("s10", "Color Repair", ServiceCategory.Coloring, 9000, 90, "", "Professional color correction (8000-10000)"),
            Service("s11", "L'Oreal Full Color", ServiceCategory.Coloring, 4750, 75, "", "Complete L'Oreal service (3500-6000)"),
            Service("s12", "Just For Men Color", ServiceCategory.Coloring, 4500, 60, "", "Men's specialized coloring (3000-6000)"),
            Service("s52", "Hair Dye", ServiceCategory.Coloring, 1500, 45, "", "Professional hair dye service"),
            
            // TREATMENTS
            Service("s13", "Protein Dose", ServiceCategory.Treatments, 3000, 60, "", "Hair protein treatment"),
            Service("s14", "Dandruff Control", ServiceCategory.Treatments, 1000, 45, "", "Anti-dandruff treatment"),
            Service("s15", "Moisture Reback", ServiceCategory.Treatments, 1000, 45, "", "Deep moisture restoration"),
            Service("s16", "Shine Enhance", ServiceCategory.Treatments, 2500, 50, "", "Hair shine enhancement treatment"),
            
            // TEXTURE SERVICES
            Service("s17", "Hair Keratin", ServiceCategory.Texture, 13000, 120, "", "Keratin smoothing treatment (6000-20000)"),
            Service("s18", "Hair Botox", ServiceCategory.Texture, 18500, 150, "", "Hair botox treatment (7000-30000)"),
            Service("s19", "Hair Perming", ServiceCategory.Texture, 19000, 180, "", "Professional hair perming (8000-30000)"),
            
            // SKIN - FACIALS
            Service("s20", "Dermalogica Facial", ServiceCategory.Skin, 10000, 90, "", "Premium Dermalogica facial treatment"),
            Service("s21", "Thalgo Facial", ServiceCategory.Skin, 8500, 75, "", "Thalgo professional facial"),
            Service("s22", "Conatural Facial", ServiceCategory.Skin, 7000, 60, "", "Natural facial treatment"),
            Service("s23", "Janssen Facial", ServiceCategory.Skin, 6500, 60, "", "Janssen skincare facial"),
            Service("s24", "Skin Care Facial", ServiceCategory.Skin, 4500, 45, "", "Basic skincare facial"),
            Service("s25", "Combination Facial", ServiceCategory.Skin, 3000, 40, "", "Combination skin treatment"),
            Service("s53", "Janssen Whitening Facial", ServiceCategory.Skin, 7000, 75, "", "Janssen whitening facial treatment"),
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
            
            // GROOMING
            Service("s42", "Party Grooming", ServiceCategory.Grooming, 4500, 90, "", "Complete party grooming package"),
            Service("s43", "Special Effects", ServiceCategory.Grooming, 3500, 75, "", "Special effects makeup (3000-4000)"),
            Service("s44", "Character Make-up", ServiceCategory.Grooming, 3750, 80, "", "Character makeup service (3000-4500)")
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
    
    val mockUser = User(
        id = "user-1",
        name = "Ahmed Khan",
        phone = "+92-300-9999999",
        email = "ahmed@example.com",
        selectedBranchId = "islamabad-1",
        totalVisits = 8,
        loyaltyStatus = LoyaltyStatus.Bronze,
        discountEligible = false,
        createdAt = Date(1705276800000) // 2024-01-15
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
            title = "New Year Membership",
            description = "Get a free service with membership",
            discount = 15,
            validFrom = Date(1704067200000), // 2025-01-01
            validTo = Date(1709251200000) // 2025-02-28
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


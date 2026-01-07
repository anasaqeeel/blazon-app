package com.blazon.app.data.model

data class Service(
    val id: String,
    val name: String,
    val category: ServiceCategory,
    val price: Int,
    val duration: Int, // in minutes
    val branchId: String,
    val description: String? = null
)

enum class ServiceCategory {
    Hair,
    Beard,
    Skin,
    Treatments,
    Texture,
    Coloring,
    Polisher,
    Massage,
    Nails,
    Wax,
    Grooming
}


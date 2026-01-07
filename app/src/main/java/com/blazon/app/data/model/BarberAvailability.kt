package com.blazon.app.data.model

import java.util.Date

data class BarberAvailability(
    val branchId: String,
    val availableBarbers: Int,
    val freeSlots: Int,
    val lastUpdated: Date
)


package com.blazon.app.data.model

// Tracks progress toward the "every 11th visit of the same service is free" reward.
// Counter goes 0..10. When paidVisits reaches 10, the NEXT visit of that service is free,
// which consumes the ready-state and resets the counter to 0.
data class ServiceProgress(
    val serviceName: String,
    val category: ServiceCategory,
    val price: Int,
    val paidVisits: Int,       // 0..10 (resets after free redemption)
    val freeRedeemed: Int,     // lifetime free services of this type already claimed
    val isFreeReady: Boolean   // true when paidVisits == 10 → next one is free
) {
    val visitsUntilFree: Int get() = if (isFreeReady) 0 else (10 - paidVisits)
    val progressFraction: Float get() = paidVisits / 10f
}

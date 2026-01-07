package com.blazon.app.data.model

data class Branch(
    val id: String,
    val name: String,
    val city: String,
    val address: String,
    val phone: String,
    val wifiNetwork: String,
    val qrCode: String,
    val operatingHours: OperatingHours
)

data class OperatingHours(
    val open: String,
    val close: String
)


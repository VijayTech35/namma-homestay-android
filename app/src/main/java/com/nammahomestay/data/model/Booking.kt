package com.nammahomestay.data.model

data class Booking(
    val id: String = "",
    val homestayId: String = "",
    val hostId: String = "",
    val guestId: String = "",
    val guestName: String = "",
    val guestPhone: String = "",
    val checkIn: String = "",
    val checkOut: String = "",
    val guests: Int = 1,
    val status: String = "pending",
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "homestayId" to homestayId,
        "hostId" to hostId,
        "guestId" to guestId,
        "guestName" to guestName,
        "guestPhone" to guestPhone,
        "checkIn" to checkIn,
        "checkOut" to checkOut,
        "guests" to guests,
        "status" to status,
        "message" to message,
        "createdAt" to createdAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Booking = Booking(
            id = map["id"] as? String ?: "",
            homestayId = map["homestayId"] as? String ?: "",
            hostId = map["hostId"] as? String ?: "",
            guestId = map["guestId"] as? String ?: "",
            guestName = map["guestName"] as? String ?: "",
            guestPhone = map["guestPhone"] as? String ?: "",
            checkIn = map["checkIn"] as? String ?: "",
            checkOut = map["checkOut"] as? String ?: "",
            guests = (map["guests"] as? Number)?.toInt() ?: 1,
            status = map["status"] as? String ?: "pending",
            message = map["message"] as? String ?: "",
            createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis()
        )
    }
}
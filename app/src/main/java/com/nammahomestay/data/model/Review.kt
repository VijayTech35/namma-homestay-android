package com.nammahomestay.data.model

data class Review(
    val id: String = "",
    val homestayId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "homestayId" to homestayId,
        "userId" to userId,
        "userName" to userName,
        "rating" to rating,
        "comment" to comment,
        "createdAt" to createdAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Review = Review(
            id = map["id"] as? String ?: "",
            homestayId = map["homestayId"] as? String ?: "",
            userId = map["userId"] as? String ?: "",
            userName = map["userName"] as? String ?: "",
            rating = (map["rating"] as? Number)?.toFloat() ?: 0f,
            comment = map["comment"] as? String ?: "",
            createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis()
        )
    }
}
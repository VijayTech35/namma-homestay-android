package com.nammahomestay.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nammahomestay.data.model.Booking
import com.nammahomestay.utils.Constants
import kotlinx.coroutines.tasks.await

class BookingRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getBookingsForHost(hostId: String): Result<List<Booking>> {
        return try {
            val snapshot = firestore.collection(Constants.BOOKINGS_COLLECTION)
                .whereEqualTo("hostId", hostId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val bookings = snapshot.documents.mapNotNull { doc ->
                Booking.fromMap(doc.data ?: return@mapNotNull null)
            }
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingsForGuest(guestId: String): Result<List<Booking>> {
        return try {
            val snapshot = firestore.collection(Constants.BOOKINGS_COLLECTION)
                .whereEqualTo("guestId", guestId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val bookings = snapshot.documents.mapNotNull { doc ->
                Booking.fromMap(doc.data ?: return@mapNotNull null)
            }
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBooking(booking: Booking): Result<String> {
        return try {
            val docRef = firestore.collection(Constants.BOOKINGS_COLLECTION).document()
            val newBooking = booking.copy(id = docRef.id)
            docRef.set(newBooking.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBookingStatus(bookingId: String, status: String): Result<Unit> {
        return try {
            firestore.collection(Constants.BOOKINGS_COLLECTION)
                .document(bookingId)
                .update("status", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
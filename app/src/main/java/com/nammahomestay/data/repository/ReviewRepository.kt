package com.nammahomestay.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nammahomestay.data.model.Review
import com.nammahomestay.utils.Constants
import kotlinx.coroutines.tasks.await

class ReviewRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getReviews(homestayId: String): Result<List<Review>> {
        return try {
            val snapshot = firestore.collection(Constants.REVIEWS_COLLECTION)
                .whereEqualTo("homestayId", homestayId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val reviews = snapshot.documents.mapNotNull { doc ->
                Review.fromMap(doc.data ?: return@mapNotNull null)
            }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAverageRating(homestayId: String): Float {
        return try {
            val snapshot = firestore.collection(Constants.REVIEWS_COLLECTION)
                .whereEqualTo("homestayId", homestayId)
                .get()
                .await()
            val ratings = snapshot.documents.mapNotNull { doc ->
                (doc.data?.get("rating") as? Number)?.toFloat()
            }
            if (ratings.isEmpty()) 0f else ratings.sum() / ratings.size
        } catch (e: Exception) {
            0f
        }
    }

    suspend fun addReview(review: Review): Result<String> {
        return try {
            val docRef = firestore.collection(Constants.REVIEWS_COLLECTION).document()
            val newReview = review.copy(id = docRef.id)
            docRef.set(newReview.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.nammahomestay.ui.guest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.nammahomestay.R
import com.nammahomestay.data.model.Review
import com.nammahomestay.data.repository.ReviewRepository
import com.nammahomestay.utils.SessionManager
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class ReviewDialogFragment : BottomSheetDialogFragment() {

    var homestayId: String = ""
    var onReviewSubmitted: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())

        val ratingBar = view.findViewById<android.widget.RatingBar>(R.id.ratingBarReview)
        val etComment = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etReviewComment)
        val btnSubmit = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSubmitReview)

        btnSubmit.setOnClickListener {
            val rating = ratingBar.rating
            val comment = etComment.text.toString().trim()

            if (rating == 0f) {
                Snackbar.make(view, "Please select a rating", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val review = Review(
                homestayId = homestayId,
                userId = sessionManager.userId,
                userName = sessionManager.userName,
                rating = rating,
                comment = comment
            )

            lifecycleScope.launch {
                val repo = ReviewRepository()
                val result = repo.addReview(review)
                result.onSuccess {
                    Snackbar.make(view, "Review submitted!", Snackbar.LENGTH_LONG).show()
                    onReviewSubmitted?.invoke()
                    dismiss()
                }.onFailure {
                    Snackbar.make(view, "Failed to submit review", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}
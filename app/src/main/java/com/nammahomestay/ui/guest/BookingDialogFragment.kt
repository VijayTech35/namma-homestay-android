package com.nammahomestay.ui.guest

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.nammahomestay.R
import com.nammahomestay.data.model.Booking
import com.nammahomestay.data.repository.BookingRepository
import com.nammahomestay.utils.SessionManager
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import java.util.Calendar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class BookingDialogFragment : BottomSheetDialogFragment() {

    var homestayId: String = ""
    var hostId: String = ""

    private var selectedCheckIn = ""
    private var selectedCheckOut = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())

        val btnCheckIn = view.findViewById<MaterialButton>(R.id.btnCheckIn)
        val btnCheckOut = view.findViewById<MaterialButton>(R.id.btnCheckOut)
        val btnConfirm = view.findViewById<MaterialButton>(R.id.btnConfirmBooking)
        val etGuests = view.findViewById<TextInputEditText>(R.id.etGuests)

        btnCheckIn.setOnClickListener {
            showDatePicker { date ->
                selectedCheckIn = date
                btnCheckIn.text = date
            }
        }

        btnCheckOut.setOnClickListener {
            showDatePicker { date ->
                selectedCheckOut = date
                btnCheckOut.text = date
            }
        }

        btnConfirm.setOnClickListener {
            if (selectedCheckIn.isBlank() || selectedCheckOut.isBlank()) {
                Snackbar.make(view, "Please select dates", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val guests = etGuests.text.toString().toIntOrNull() ?: 1
            val booking = Booking(
                homestayId = homestayId,
                hostId = hostId,
                guestId = sessionManager.userId,
                guestName = sessionManager.userName,
                guestPhone = sessionManager.userPhone,
                checkIn = selectedCheckIn,
                checkOut = selectedCheckOut,
                guests = guests
            )
            lifecycleScope.launch {
                val repo = BookingRepository()
                val result = repo.createBooking(booking)
                result.onSuccess {
                    Snackbar.make(view, "Booking request sent!", Snackbar.LENGTH_LONG).show()
                    dismiss()
                }.onFailure {
                    Snackbar.make(view, "Booking failed", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val cal = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            onDateSelected(String.format("%04d-%02d-%02d", y, m + 1, d))
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }
}
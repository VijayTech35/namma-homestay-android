package com.nammahomestay.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nammahomestay.R
import com.nammahomestay.data.repository.AuthRepository
import com.nammahomestay.databinding.FragmentLoginBinding
import com.nammahomestay.utils.ValidationUtils
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authRepository = AuthRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendOtp.setOnClickListener {
            val phone = binding.etPhone.text.toString().trim()
            if (ValidationUtils.isValidPhone(phone)) {
                sendOtp(phone)
            } else {
                binding.etPhone.error = "Enter a valid phone number"
            }
        }
    }

    private fun sendOtp(phone: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSendOtp.isEnabled = false

        val formattedPhone = if (phone.startsWith("+")) phone else "+91$phone"

        authRepository.sendOtp(
            phoneNumber = formattedPhone,
            onCodeSent = { verificationId ->
                binding.progressBar.visibility = View.GONE
                val bundle = Bundle().apply {
                    putString("verificationId", verificationId)
                    putString("phone", formattedPhone)
                }
                findNavController().navigate(R.id.action_login_to_otp, bundle)
            },
            onError = { error ->
                binding.progressBar.visibility = View.GONE
                binding.btnSendOtp.isEnabled = true
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

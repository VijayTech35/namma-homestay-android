package com.nammahomestay.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nammahomestay.R
import com.nammahomestay.data.model.User
import com.nammahomestay.data.repository.AuthRepository
import com.nammahomestay.databinding.FragmentRoleSelectionBinding
import com.nammahomestay.utils.Constants
import com.nammahomestay.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoleSelectionFragment : Fragment() {

    private var _binding: FragmentRoleSelectionBinding? = null
    private val binding get() = _binding!!
    private val authRepository = AuthRepository()
    private lateinit var sessionManager: SessionManager
    private var selectedRole: String = ""
    private var phone: String = ""
    private var googleUid: String = ""
    private var googleName: String = ""
    private var googleEmail: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoleSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        phone = arguments?.getString("phone") ?: ""
        googleUid = arguments?.getString("uid") ?: ""
        googleName = arguments?.getString("name") ?: ""
        googleEmail = arguments?.getString("email") ?: ""

        binding.cardGuest.setOnClickListener {
            selectedRole = "guest"
            updateSelection()
        }

        binding.cardHost.setOnClickListener {
            selectedRole = "host"
            updateSelection()
        }

        binding.btnContinue.setOnClickListener {
            if (selectedRole.isNotBlank()) {
                saveUserAndProceed()
            } else {
                Snackbar.make(binding.root, "Please select a role", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSelection() {
        binding.cardGuest.setCardBackgroundColor(
            if (selectedRole == "guest") {
                resources.getColor(R.color.selected_card, null)
            } else {
                resources.getColor(R.color.default_card, null)
            }
        )
        binding.cardHost.setCardBackgroundColor(
            if (selectedRole == "host") {
                resources.getColor(R.color.selected_card, null)
            } else {
                resources.getColor(R.color.default_card, null)
            }
        )
    }

    private fun saveUserAndProceed() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnContinue.isEnabled = false

        val uid = if (googleUid.isNotBlank()) googleUid
        else FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val name = if (googleName.isNotBlank()) googleName else "User"

        val user = User(
            uid = uid,
            phone = if (phone.isNotBlank()) phone else googleEmail,
            name = name,
            role = selectedRole
        )

        // Save to session immediately
        sessionManager.saveUser(uid, user.phone, name, selectedRole)

        // Navigate immediately (don't wait for Firestore)
        when (selectedRole) {
            "guest" -> findNavController().navigate(R.id.action_roleSelection_to_guestHome)
            "host" -> findNavController().navigate(R.id.action_roleSelection_to_hostDashboard)
        }

        // Firestore save + FCM in background
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                authRepository.saveUserToFirestore(user)
            } catch (_: Exception) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
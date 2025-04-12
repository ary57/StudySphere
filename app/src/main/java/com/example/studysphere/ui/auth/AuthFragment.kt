package com.example.studysphere.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studysphere.R
import com.example.studysphere.databinding.FragmentAuthBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set up the tab layout
        setupTabLayout()

        // Set up button click listeners
        setupClickListeners()
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.loginLayout.visibility = View.VISIBLE
                        binding.registerLayout.visibility = View.GONE
                    }
                    1 -> {
                        binding.loginLayout.visibility = View.GONE
                        binding.registerLayout.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupClickListeners() {
        // Login button click listener
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailEditText.text.toString().trim()
            val password = binding.loginPasswordEditText.text.toString().trim()

            if (validateLoginInputs(email, password)) {
                showLoading(true)
                loginUser(email, password)
            }
        }

        // Register button click listener
        binding.registerButton.setOnClickListener {
            val name = binding.registerNameEditText.text.toString().trim()
            val email = binding.registerEmailEditText.text.toString().trim()
            val password = binding.registerPasswordEditText.text.toString().trim()

            if (validateRegisterInputs(name, email, password)) {
                showLoading(true)
                registerUser(name, email, password)
            }
        }
    }

    private fun validateLoginInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.loginEmailEditText.error = "Email is required"
            binding.loginEmailEditText.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.loginPasswordEditText.error = "Password is required"
            binding.loginPasswordEditText.requestFocus()
            return false
        }

        return true
    }

    private fun validateRegisterInputs(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            binding.registerNameEditText.error = "Name is required"
            binding.registerNameEditText.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            binding.registerEmailEditText.error = "Email is required"
            binding.registerEmailEditText.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.registerPasswordEditText.error = "Password is required"
            binding.registerPasswordEditText.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.registerPasswordEditText.error = "Password should be at least 6 characters"
            binding.registerPasswordEditText.requestFocus()
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    // Login successful
                    navigateToMainActivity()
                } else {
                    // Login failed
                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful, update display name
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            showLoading(false)
                            if (profileTask.isSuccessful) {
                                // Profile updated successfully
                                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                                navigateToMainActivity()
                            } else {
                                // Profile update failed
                                Toast.makeText(context, "Failed to update profile: ${profileTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Registration failed
                    showLoading(false)
                    Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !isLoading
        binding.registerButton.isEnabled = !isLoading
    }

    private fun navigateToMainActivity() {
        // Navigate to the main activity using Navigation Component
        findNavController().navigate(R.id.action_auth_to_main)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
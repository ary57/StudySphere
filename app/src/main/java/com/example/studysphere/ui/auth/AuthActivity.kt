package com.example.studysphere.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.studysphere.MainActivity
import com.example.studysphere.R
import com.google.firebase.auth.FirebaseAuth

/**
 * Activity to host the authentication fragment.
 * This serves as a container for the login/register functionality.
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, go to MainActivity
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close AuthActivity so user can't go back without logging out
    }
}
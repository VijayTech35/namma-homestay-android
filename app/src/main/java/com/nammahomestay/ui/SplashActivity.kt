package com.nammahomestay.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.shimmer.ShimmerFrameLayout
import com.nammahomestay.MainActivity
import com.nammahomestay.R
import com.nammahomestay.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    private lateinit var shimmerLayout: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        shimmerLayout = findViewById(R.id.shimmerLayout)
        shimmerLayout.startShimmer()
        requestNotificationPermission()

        Handler(Looper.getMainLooper()).postDelayed({
            shimmerLayout.stopShimmer()
            val intent = Intent(this, MainActivity::class.java)
            val sessionManager = SessionManager(this)
            if (sessionManager.isLoggedIn) {
                intent.putExtra("auto_role", sessionManager.getUserRole())
            }
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (::shimmerLayout.isInitialized) shimmerLayout.stopShimmer()
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1001
    }
}
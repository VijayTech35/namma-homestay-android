package com.nammahomestay.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nammahomestay.MainActivity
import com.nammahomestay.R
import com.nammahomestay.utils.SessionManager

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SessionManager(this).fcmToken = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: message.data["title"] ?: "Namma HomeStay"
        val body = message.notification?.body ?: message.data["body"] ?: ""

        if (body.isNotBlank()) {
            showNotification(title, body, message.data)
        }
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", data["screen"])
            putExtra("homestayId", data["homestayId"])
            putExtra("inquiryId", data["inquiryId"])
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT)
            .setSmallIcon(R.drawable.ic_homestay)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val defaultChannel = NotificationChannel(
                CHANNEL_DEFAULT,
                "General Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "New inquiries, bookings, and updates"
                enableVibration(true)
            }

            val channelInquiry = NotificationChannel(
                CHANNEL_INQUIRY,
                "Inquiries",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "New guest inquiries"
                enableVibration(true)
            }

            val channelBooking = NotificationChannel(
                CHANNEL_BOOKING,
                "Bookings",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "New booking confirmations"
                enableVibration(true)
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(defaultChannel)
            notificationManager.createNotificationChannel(channelInquiry)
            notificationManager.createNotificationChannel(channelBooking)
        }
    }

    companion object {
        const val CHANNEL_DEFAULT = "namma_homestay_default"
        const val CHANNEL_INQUIRY = "namma_homestay_inquiry"
        const val CHANNEL_BOOKING = "namma_homestay_booking"
    }
}

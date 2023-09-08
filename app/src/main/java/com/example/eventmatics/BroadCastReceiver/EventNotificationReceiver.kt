package com.example.eventmatics.BroadCastReceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.Random

class EventNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val eventName = intent?.getStringExtra("event_name")
        Log.d("EventGetName", eventName ?: "Event name is null or empty")

        if (!eventName.isNullOrEmpty()) {
            showEventNotification(context, eventName)
        }
    }

    private fun showEventNotification(context: Context?, eventName: String) {
        val channelId = "Event_Notification"
        val notificationId = Random().nextInt(1000)

        // Create a notification channel (Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Event Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)

            val notificationManager = context?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context!!, channelId)
            .setContentTitle("Event Reminder")
            .setContentText("Your Event $eventName is tomorrow!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setColor(ContextCompat.getColor(context!!, android.R.color.darker_gray))

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }
}

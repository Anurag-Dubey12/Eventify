package com.example.eventmatics

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.os.CountDownTimer
import android.widget.RemoteViews
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [EventWidgetConfigureActivity]
 */
class EventWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
fun getSharedPreference(context: Context, key: String): String? {
    val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
    return sharedPref.getString(key, null)
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val views = RemoteViews(context.packageName, R.layout.event_widget)
    val databaseHelper = DatabaseManager.getDatabase(context)
    val eventTimer = databaseHelper.getEventData(1)

    if (eventTimer != null) {
        val name = eventTimer.name
        val date = eventTimer.Date
        val time = eventTimer.time
        views.setTextViewText(R.id.eventname, name)
        views.setTextViewText(R.id.eventdate, date)

        // Display the initial event time
        views.setTextViewText(R.id.eventtime, time)

        // Calculate and display the countdown timer
        updateCountdownTimer(context, views, time)
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private var countDownTimer: CountDownTimer? = null

private fun updateCountdownTimer(context: Context, views: RemoteViews, eventTime: String) {
    val currentDate = Calendar.getInstance()

    // Parse the time part of eventTime
    val eventTimeParts = eventTime.split(":")
    if (eventTimeParts.size == 3) {
        val hours = eventTimeParts[0].toInt()
        val minutes = eventTimeParts[1].toInt()
        val seconds = eventTimeParts[2].toInt()

        // Set the time in currentDate
        currentDate.set(Calendar.HOUR_OF_DAY, hours)
        currentDate.set(Calendar.MINUTE, minutes)
        currentDate.set(Calendar.SECOND, seconds)

        val eventDateTime = currentDate.time

        val remainingTimeInMillis = eventDateTime.time - Calendar.getInstance().timeInMillis

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(remainingTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val days = millisUntilFinished / (24 * 60 * 60 * 1000)
                val hours = (millisUntilFinished % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)
                val minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val remainingTime =
                    String.format("%02dd %02dh %02dm %02ds", days, hours, minutes, seconds)

                // Update the eventtime TextView with the countdown timer
                views.setTextViewText(R.id.eventtime, remainingTime)

                // Instruct the widget manager to update the widget
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, EventWidget::class.java)
                )

                appWidgetManager.updateAppWidget(appWidgetIds, views)
            }

            override fun onFinish() {
                views.setTextViewText(R.id.eventtime, "Event Started")

                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, EventWidget::class.java)
                )

                appWidgetManager.updateAppWidget(appWidgetIds, views)
            }
        }.start()
    } else {
        // Handle the case where eventTime is not in the expected format
        views.setTextViewText(R.id.eventtime, "Invalid Date/Time")
    }
}
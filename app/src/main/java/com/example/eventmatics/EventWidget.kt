package com.example.eventmatics

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.os.CountDownTimer
import android.widget.RemoteViews
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

class EventWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) { updateAppWidget(context, appWidgetManager, appWidgetId) }
    }
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) { deleteTitlePref(context, appWidgetId) }
    }

    override fun onEnabled(context: Context) {}
    override fun onDisabled(context: Context) {}
}
fun getSharedPreference(context: Context, key: String): String? {
    val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
    return sharedPref.getString(key, null)
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.event_widget)
    GlobalScope.launch(Dispatchers.IO){

    val databaseHelper = RoomDatabaseManager.getDatabase(context)
    val dao=databaseHelper.eventdao()
    val eventTimer = dao.getEventData(1)

    if (eventTimer != null) {
        val name = eventTimer.name
        val date = eventTimer.date
        val time = eventTimer.time
        views.setTextViewText(R.id.eventname, name)
        views.setTextViewText(R.id.eventdate, date)
        views.setTextViewText(R.id.eventtime, time)
        updateCountdownTimer(context, views, time)
    }
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
    }
private var countDownTimer: CountDownTimer? = null

private fun updateCountdownTimer(context: Context, views: RemoteViews, eventTime: String) {
    val currentDate = Calendar.getInstance()

    val eventTimeParts = eventTime.split(":")
    if (eventTimeParts.size == 3) {
        val hours = eventTimeParts[0].toInt()
        val minutes = eventTimeParts[1].toInt()
        val seconds = eventTimeParts[2].toInt()

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
                views.setTextViewText(R.id.eventtime, remainingTime)
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, EventWidget::class.java))
                appWidgetManager.updateAppWidget(appWidgetIds, views)
            }
            override fun onFinish() {
                views.setTextViewText(R.id.eventtime, "Event Started")
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, EventWidget::class.java))
                appWidgetManager.updateAppWidget(appWidgetIds, views) }
        }.start()
    } else {
        views.setTextViewText(R.id.eventtime, "Invalid Date/Time")
    }
}
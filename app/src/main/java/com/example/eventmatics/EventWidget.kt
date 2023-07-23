package com.example.eventmatics

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.widget.RemoteViews
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase

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


//    val widgetText = loadTitlePref(context, appWidgetId)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.event_widget)
//    views.setTextViewText(R.id.appwidget_text, widgetText)
    val databasename=getSharedPreference(context,"databasename").toString()
    val databaseHelper = LocalDatabase(context, databasename)
    val Eventtimer = databaseHelper.getEventData(1)

    if(Eventtimer!=null){
        val name=Eventtimer.name
        val date=Eventtimer.Date
        val time=Eventtimer.time
        views.setTextViewText(R.id.eventname, name)
        views.setTextViewText(R.id.eventdate, date)
        views.setTextViewText(R.id.eventtime, time)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
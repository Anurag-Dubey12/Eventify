package com.example.eventmatics

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.eventmatics.databinding.EventWidgetConfigureBinding
class EventWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var appWidgetText: EditText
    private var onClickListener = View.OnClickListener {
        val context = this@EventWidgetConfigureActivity

        val widgetText = appWidgetText.text.toString()
        saveTitlePref(context, appWidgetId, widgetText)

        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
    private lateinit var binding: EventWidgetConfigureBinding

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setResult(RESULT_CANCELED)

        binding = EventWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appWidgetText = binding.appwidgetText as EditText
        binding.addButton.setOnClickListener(onClickListener)
        val intent = intent
        val extras = intent.extras
        if (extras != null) { appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) { finish()
            return
        }
        appWidgetText.setText(loadTitlePref(this@EventWidgetConfigureActivity, appWidgetId))
    }

}

private const val PREFS_NAME = "com.example.eventmatics.EventWidget"
private const val PREF_PREFIX_KEY = "appwidget_"
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    prefs.apply()
}
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    return titleValue ?: context.getString(R.string.appwidget_text)
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}
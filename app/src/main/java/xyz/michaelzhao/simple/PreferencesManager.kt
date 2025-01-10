package xyz.michaelzhao.simple

import android.content.Context

// Preferences Manager
class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)

    fun saveNumber(widgetId: Int, number: Int) {
        prefs.edit().putInt(getWidgetKey(widgetId), number).apply()
    }

    fun getNumber(widgetId: Int): Int {
        return prefs.getInt(getWidgetKey(widgetId), 1)
    }

    private fun getWidgetKey(widgetId: Int): String {
        return "widget_num_$widgetId"
    }

    fun removeWidget(widgetId: Int) {
        prefs.edit().remove(getWidgetKey(widgetId)).apply()
    }
}

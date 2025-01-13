package xyz.michaelzhao.simple

import android.content.Context

// Preferences Manager
class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)

    fun saveNumber(widgetId: Int, number: Int) {
        prefs.edit().putInt(getWidgetKey("widget_num_", widgetId), number).apply()
    }

    fun getNumber(widgetId: Int): Int {
        return prefs.getInt(getWidgetKey("widget_num_", widgetId), 1)
    }

    fun saveHidePage(widgetId: Int, value: Boolean) {
        prefs.edit().putBoolean(getWidgetKey("widget_hide_", widgetId), value).apply()
    }

    fun getHidePage(widgetId: Int): Boolean {
        return prefs.getBoolean(getWidgetKey("widget_hide_", widgetId), false)
    }

    fun saveFontSize(widgetId: Int, fontSize: Float) {
        prefs.edit().putFloat(getWidgetKey("widget_font_", widgetId), fontSize).apply()
    }

    fun getFontSize(widgetId: Int): Float {
        return prefs.getFloat(getWidgetKey("widget_font_", widgetId), 30f)
    }

    fun getAppData(): String? {
        return prefs.getString("data", "")
    }

    fun saveAppData(data: String) {
        prefs.edit().putString("data", data).apply()
    }

    private fun getWidgetKey(pre: String, widgetId: Int): String {
        return "$pre$widgetId"
    }

    fun removeWidget(widgetId: Int) {
        prefs.edit().remove(getWidgetKey("widget_num_", widgetId)).apply()
        prefs.edit().remove(getWidgetKey("Widget_hide_", widgetId)).apply()
    }
}

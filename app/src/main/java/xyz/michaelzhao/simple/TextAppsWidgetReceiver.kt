package xyz.michaelzhao.simple

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TextAppsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TextAppsWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val preferencesManager = PreferencesManager(context)
        appWidgetIds.forEach { widgetId ->
            preferencesManager.removeWidget(widgetId)
        }
    }
}

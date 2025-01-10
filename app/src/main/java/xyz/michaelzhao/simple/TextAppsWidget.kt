package xyz.michaelzhao.simple

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.glance.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class TextAppsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val num = PreferencesManager(context).getNumber(appWidgetId)

        updateAppWidgetState(context, id) { prefs ->
            prefs[intPreferencesKey("widget_number")] = num
        }

        provideContent {
            val numState = currentState<Preferences>()[intPreferencesKey("widget_number")]

            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "App List $numState",
                    style = TextStyle(color = ColorProvider(Color.White))
                )
                Button(
                    text = "Gmail",
                    onClick = actionStartActivity(createLaunchIntent(context, "com.google.android.gm"))
                )
                Button(
                    text = "Discord",
                    onClick = actionStartActivity(createLaunchIntent(context, "com.discord"))
                )
            }
        }
    }

    private fun createLaunchIntent(context: Context, packageName: String): Intent {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)

        return launchIntent ?: Intent().apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            // Optionally show a toast when app isn't found
            Log.e("onLaunchIntent", "App not found: $packageName")
        }
    }
}
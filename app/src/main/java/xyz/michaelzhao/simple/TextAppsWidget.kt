package xyz.michaelzhao.simple

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.glance.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.components.FilledButton
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.material.ColorProviders
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import xyz.michaelzhao.simple.ui.theme.DarkColorScheme
import xyz.michaelzhao.simple.ui.theme.LightColorScheme

class TextAppsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val num = PreferencesManager(context).getNumber(appWidgetId)

        updateAppWidgetState(context, id) { prefs ->
            prefs[intPreferencesKey("widget_number")] = num
        }

        provideContent {
            val numState = currentState<Preferences>()[intPreferencesKey("widget_number")]

            GlanceTheme {
                Column(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    AppButton(
                        context = context,
                        display = "Gmail",
                        packageName = "com.google.android.gm"
                    )
                    AppButton(context = context, display = "Discord", packageName = "com.discord")
                    Text(
                        "App List $numState",
                        modifier = GlanceModifier.padding(top = 4.dp),
                        style = TextStyle(color = ColorProvider(Color.Gray))
                    )
                }
            }
        }
    }

    @Composable
    private fun AppButton(context: Context, display: String, packageName: String) {
        Text(
            text = display,
            modifier = GlanceModifier.clickable(
                actionStartActivity(
                    createLaunchIntent(
                        context,
                        packageName
                    )
                )
            ).padding(horizontal = 4.dp),
            style = TextStyle(
                color = GlanceTheme.colors.secondary,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )
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

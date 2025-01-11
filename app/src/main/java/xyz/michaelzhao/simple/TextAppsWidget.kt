package xyz.michaelzhao.simple

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class TextAppsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val num = PreferencesManager(context).getNumber(appWidgetId)

        updateAppWidgetState(context, id) { prefs ->
            prefs[intPreferencesKey("widget_number")] = num
            prefs[booleanPreferencesKey("hide_page_num")] = false
            prefs[intPreferencesKey("version")] = 0
        }

        provideContent {
            GlanceTheme {
                WidgetContent(context)
            }
        }
    }

    @Composable
    private fun WidgetContent(context: Context) {
        val dm = remember { DataManager(context) }
        val data = dm.loadData()
        val numState = currentState<Preferences>()[intPreferencesKey("widget_number")] ?: 0
        val hidePageNum =
            currentState<Preferences>()[booleanPreferencesKey("hide_page_num")] ?: false

        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (numState < data.size) {
                data[numState].map {
                    AppButton(context = context, display = it.first, packageName = it.second)
                }
            }
            if (!hidePageNum) {
                Text(
                    "App List $numState",
                    modifier = GlanceModifier.padding(top = 4.dp),
                    style = TextStyle(color = ColorProvider(Color.Gray))
                )
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

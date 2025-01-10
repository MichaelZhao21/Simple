package xyz.michaelzhao.simple

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.glance.layout.Column
import androidx.compose.runtime.Composable
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text

class TextAppsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MyContent(context)
        }
    }

    @Composable
    private fun MyContent(context: Context) {
        Column (
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "App List")
            Button(text = "Gmail", onClick = actionStartActivity(createLaunchIntent(context, "com.google.android.gm")))
            Button(text = "Discord", onClick = actionStartActivity(createLaunchIntent(context, "com.discord")))
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
package xyz.michaelzhao.simple

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class TextAppsWidgetConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appWidgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
            ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setResult(RESULT_CANCELED)

        setContent {
            MaterialTheme {
                ConfigurationScreen(
                    onSave = { numberText: String ->
                        // Save number
                        val number =
                            numberText.toInt() // TODO: catch conversion error or make sure it doesn't happen
                        PreferencesManager(this).saveNumber(appWidgetId, number)

                        // Update widget
                        lifecycleScope.launch {
                            val glanceId = GlanceAppWidgetManager(this@TextAppsWidgetConfigActivity)
                                .getGlanceIdBy(appWidgetId)
                            TextAppsWidget().update(this@TextAppsWidgetConfigActivity, glanceId)
                        }

                        // Set result to OK
                        val resVal = Intent().apply {
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                        }
                        setResult(RESULT_OK, resVal)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun ConfigurationScreen(onSave: (String) -> Unit) {
    var numberText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = numberText,
            onValueChange = { numberText = it },
            label = { Text("App Display Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button({ onSave(numberText) }, enabled = numberText.isNotBlank()) {
            Text("Save")
        }
    }
}

package xyz.michaelzhao.simple

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import xyz.michaelzhao.simple.ui.theme.SimpleTheme
import java.util.Date

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

        val glanceId =
            GlanceAppWidgetManager(this@TextAppsWidgetConfigActivity)
                .getGlanceIdBy(appWidgetId)
        val preferencesManager = PreferencesManager(this@TextAppsWidgetConfigActivity)

        val prevNumber = preferencesManager.getNumber(appWidgetId)
        val prevHide = preferencesManager.getHidePage(appWidgetId)

        setResult(RESULT_CANCELED)

        setContent {
            MaterialTheme {
                ConfigurationScreen(
                    prevNumber,
                    prevHide,
                    onSave = { numberText: String, hidePageNumber: Boolean ->
                        lifecycleScope.launch {
                            // Save number
                            val number =
                                numberText.toInt() // TODO: catch conversion error or make sure it doesn't happen
                            preferencesManager.saveNumber(appWidgetId, number)
                            preferencesManager.saveHidePage(appWidgetId, hidePageNumber)

                            // Update widget
                            try {
                                updateAppWidgetState(
                                    this@TextAppsWidgetConfigActivity,
                                    glanceId
                                ) { prefs ->
                                    prefs[intPreferencesKey("widget_number")] = number
                                    prefs[booleanPreferencesKey("hide_page_num")] = hidePageNumber
                                    prefs[intPreferencesKey("version")] = Date().time.toInt()
                                }
                                TextAppsWidget().update(this@TextAppsWidgetConfigActivity, glanceId)
                            } catch (e: Exception) {
                                Log.e("Widget Config", "Update failed", e)
                            }
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
fun ConfigurationScreen(prevNumber: Int, prevHide: Boolean, onSave: (String, Boolean) -> Unit) {
    var numberText by remember { mutableStateOf(prevNumber.toString()) }
    var hidePageNumber by remember { mutableStateOf(prevHide) }

    SimpleTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Text(
                        text = "Widget Configuration",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = numberText,
                        onValueChange = { numberText = it },
                        label = { Text("App Display Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = hidePageNumber,
                            onCheckedChange = { hidePageNumber = it })
                        Text("Hide Page Number")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        { onSave(numberText, hidePageNumber) },
                        enabled = numberText.isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

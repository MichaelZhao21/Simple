package xyz.michaelzhao.simple

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import xyz.michaelzhao.simple.ui.theme.SimpleTheme

class SelectAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            SimpleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SelectContext(innerPadding = innerPadding)
                }
            }
        }
    }

    @Composable
    fun SelectContext(innerPadding: PaddingValues) {
        var packages by remember { mutableStateOf(listOf<Pair<String, String>>()) }
        val selected = remember { mutableStateListOf<Boolean>() }
        val deleteDialog = remember { mutableStateOf(false) }

        val context = LocalContext.current
        val editNum = intent.extras?.getInt("index") ?: -1
        val red = MaterialTheme.colorScheme.error

        // Load all apps
        LaunchedEffect(Unit) {
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val appList = packageManager.queryIntentActivities(mainIntent, 0)
            val loadedPackages = appList.map {
                Pair(
                    it.activityInfo.applicationInfo.loadLabel(packageManager) as String,
                    it.activityInfo.packageName as String,
                )
            }
            val sortedPackages = loadedPackages.sortedBy { it.first }
            packages = sortedPackages
            selected.addAll(List(packages.size) { false })
        }

        // Loading if packages not loaded
        if (packages.isEmpty()) {
            Text(
                "Loading...",
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            return
        }

        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn {
                itemsIndexed(packages) { i, pack ->
                    PackageEntry(pack.first, selected[i], onCheckedChange = { selected[i] = it })
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (editNum != -1) {
                    OutlinedButton(
                        onClick = { deleteDialog.value = true },
                        shape = RoundedCornerShape(50),
                        colors = ButtonColors(
                            contentColor = red,
                            containerColor = Color.Transparent,
                            disabledContentColor = red,
                            disabledContainerColor = red
                        ),
                        border = BorderStroke(width = 1.dp, color = red)
                    ) {
                        Text("Delete")
                    }
                }
                Button(
                    onClick = {
                        val newData = packages.filterIndexed { i, _ -> selected[i] }

                        DataManager(context).editOrAddEntry(newData, editNum)

                        context.startActivity(Intent(context, MainActivity::class.java))
                    },
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Save " + selected.count { it }.toString() + " apps",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

            }
        }
    }

    @Composable
    private fun PackageEntry(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
            Text(
                label,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { onCheckedChange(!checked) })
        }
    }

}

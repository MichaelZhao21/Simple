package xyz.michaelzhao.simple

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.michaelzhao.simple.ui.theme.SimpleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SimpleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Simple Phone Homepage",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )
                        PageList()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OpenSelectApp()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OpenSelectApp(modifier: Modifier = Modifier, index: Int = -1) {
    val context = LocalContext.current

    fun onClick() {
        val intent = Intent(context, SelectAppActivity::class.java)
        intent.putExtra("index", index)
        context.startActivity(intent)
    }

    if (index == -1) {
        OutlinedButton(
            onClick = { onClick() },
            modifier = modifier
        ) {
            Text("New Page")
        }
        return
    }

    Text(
        "Edit",
        modifier = Modifier
            .padding(top = 4.dp)
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun PageList() {
    val data = DataManager(LocalContext.current).loadData()
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 156.dp)) {
        itemsIndexed(data) { i, d ->
            PageDisplay(i, d)
        }
    }
}

@Composable
fun PageDisplay(index: Int, data: List<Pair<String, String>>) {
    Card(
        modifier = Modifier
            .padding(all = 4.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(all = 12.dp)
        ) {
            Text("Page $index", fontSize = 14.sp, color = Color.Gray)
            data.map {
                Text(it.first, fontSize = 18.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OpenSelectApp(index = index)
            }
        }
    }
}

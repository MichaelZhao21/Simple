package xyz.michaelzhao.simple

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import xyz.michaelzhao.simple.ui.theme.SimpleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.d("data", PreferencesManager(baseContext).getAppData() ?: "")

        setContent {
            SimpleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize()
                    ) {
                        OpenSelectApp()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun OpenSelectApp() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(context, SelectAppActivity::class.java)
        intent.putExtra("index", -1)
        context.startActivity(intent)
    }) {
        Text("Select App")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleTheme {
        Greeting("Android")
    }
}
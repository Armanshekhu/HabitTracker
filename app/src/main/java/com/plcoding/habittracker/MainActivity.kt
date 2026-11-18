package com.plcoding.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
<<<<<<< HEAD
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.plcoding.habittracker.ui.navigation.NavGraph
import com.plcoding.habittracker.ui.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
=======
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.plcoding.habittracker.ui.theme.HabitTrackerTheme

>>>>>>> 4becd9350ed6b5f76fdee7bae360b5479df6f69b
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerTheme {
<<<<<<< HEAD
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F0F15)
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
=======
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
>>>>>>> 4becd9350ed6b5f76fdee7bae360b5479df6f69b
                }
            }
        }
    }
}
<<<<<<< HEAD
=======

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HabitTrackerTheme {
        Greeting("Android")
    }
}
>>>>>>> 4becd9350ed6b5f76fdee7bae360b5479df6f69b

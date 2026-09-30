package com.example.servexa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.servexa.navigation.ServexaNavGraph
import com.example.servexa.ui.theme.ServexaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ServexaTheme {

                val navController = rememberNavController()

                ServexaNavGraph(
                    navController = navController
                )
            }
        }
    }
}
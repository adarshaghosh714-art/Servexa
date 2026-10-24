
package com.example.servexa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.servexa.Setting.ThemeMode
import com.example.servexa.Setting.ThemePreferences
import com.example.servexa.navigation.ServexaNavGraph

import com.example.servexa.ui.theme.ServexaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current

            // Create the object that reads the saved theme preference.
            val themePreferences = ThemePreferences(context)

            // Observe the saved theme mode.
            val themeMode by themePreferences.themeMode.collectAsState(
                initial = ThemeMode.SYSTEM
            )

            // Read the phone's current theme.
            val systemDarkTheme = isSystemInDarkTheme()

            // Decide which theme Servexa should use.
            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> systemDarkTheme
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            ServexaTheme(
                darkTheme = darkTheme
            ) {
                val navController = rememberNavController()

                ServexaNavGraph(
                    navController = navController
                )
            }
        }
    }
}

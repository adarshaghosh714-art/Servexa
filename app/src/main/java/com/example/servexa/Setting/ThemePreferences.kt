package com.example.servexa.Setting



import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "servexa_settings"
)

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

class ThemePreferences(
    private val context: Context
) {
    companion object {
        private val THEME_MODE_KEY =
            stringPreferencesKey("theme_mode")
    }

    val themeMode: Flow<ThemeMode> =
        context.dataStore.data.map { preferences ->
            val savedMode = preferences[THEME_MODE_KEY]

            runCatching {
                ThemeMode.valueOf(savedMode ?: "SYSTEM")
            }.getOrDefault(ThemeMode.SYSTEM)
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }
}

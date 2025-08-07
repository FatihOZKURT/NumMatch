package com.example.nummatch.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.nummatch.data.datastore.DataStoreManager.PreferencesKeys.DARK_THEME_KEY
import com.example.nummatch.data.datastore.DataStoreManager.PreferencesKeys.LAST_DIFFICULTY_KEY
import com.example.nummatch.data.datastore.DataStoreManager.PreferencesKeys.RECENT_USERNAMES_KEY
import com.example.nummatch.data.datastore.DataStoreManager.PreferencesKeys.TIMER_VISIBLE_KEY
import com.example.nummatch.util.Difficulty
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val TIMER_VISIBLE_KEY = booleanPreferencesKey("show_timer")
        val LAST_DIFFICULTY_KEY = stringPreferencesKey("last_difficulty")
        val RECENT_USERNAMES_KEY = stringPreferencesKey("recent_usernames")
    }

    suspend fun getLastPlayedDifficulty(): Difficulty? {
        return dataStore.data.first()[LAST_DIFFICULTY_KEY]?.let {
            try {
                Difficulty.valueOf(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecentUsernames(): List<String> {
        val json = dataStore.data.first()[RECENT_USERNAMES_KEY] ?: return emptyList()
        return try {
            json.split(",").filter { it.isNotBlank() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveRecentUsernames(usernames: List<String>) {
        dataStore.edit { preferences ->
            preferences[RECENT_USERNAMES_KEY] = usernames.joinToString(",")
        }
    }

    val isDarkThemeFlow: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[DARK_THEME_KEY] ?: false }
        .catch { emit(false) }

    val isTimerVisibleFlow: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[TIMER_VISIBLE_KEY] ?: true }
        .catch { emit(true) }


    suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }

    suspend fun setShowTimer(isVisible: Boolean) {
        dataStore.edit { preferences ->
            preferences[TIMER_VISIBLE_KEY] = isVisible
        }
    }


}
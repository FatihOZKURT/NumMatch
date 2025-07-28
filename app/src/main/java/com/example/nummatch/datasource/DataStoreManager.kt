package com.example.nummatch.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class DataStoreManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore

    val isDarkTheme: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.DARK_THEME] ?: false }

    val isTimerVisible: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.SHOW_TIMER] ?: true }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { it[PreferencesKeys.DARK_THEME] = enabled }
    }

    suspend fun setShowTimer(visible: Boolean) {
        dataStore.edit { it[PreferencesKeys.SHOW_TIMER] = visible }
    }

    private object PreferencesKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val SHOW_TIMER = booleanPreferencesKey("show_timer")
    }
}
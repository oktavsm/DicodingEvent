package com.dicoding.dicodingevent.ui.setting

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context get() = getApplication()

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val REMINDER_KEY = booleanPreferencesKey("daily_reminder")
    }

    val isDarkMode: LiveData<Boolean> = context.dataStore.data
        .map { prefs -> prefs[DARK_MODE_KEY] ?: false }
        .asLiveData()

    val isDailyReminderEnabled: LiveData<Boolean> = context.dataStore.data
        .map { prefs -> prefs[REMINDER_KEY] ?: false }
        .asLiveData()

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[DARK_MODE_KEY] = enabled
            }
        }
    }

    fun setDailyReminder(enabled: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[REMINDER_KEY] = enabled
            }
        }
    }
}

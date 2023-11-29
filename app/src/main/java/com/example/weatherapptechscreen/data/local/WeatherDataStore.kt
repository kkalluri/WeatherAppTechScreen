package com.example.weatherapptechscreen.data.local

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 */
// WeatherDataStore.kt
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("weather_preferences")

// WeatherDataStore class responsible for handling data storage related to weather preferences.
class WeatherDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.dataStore

    // Coroutine function to save the last searched city in the data store.
    suspend fun saveLastSearchedCity(cityName: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[LAST_SEARCHED_CITY_KEY] = cityName
            }

        }
    }
    // Coroutine function to retrieve the last searched city from the data store.
    suspend fun getLastSearchedCity(): String {
        val preferences = dataStore.data.first()
        return preferences[LAST_SEARCHED_CITY_KEY] ?: DEFAULT_CITY
    }

    companion object {
        private val LAST_SEARCHED_CITY_KEY = stringPreferencesKey("last_searched_city")
        private const val DEFAULT_CITY = "" // Set your default city
    }
}


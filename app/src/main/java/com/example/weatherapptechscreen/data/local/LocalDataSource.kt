package com.example.weatherapptechscreen.data.local

import javax.inject.Inject

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 *
 */
class LocalDataSource @Inject constructor(private val weatherDataStore: WeatherDataStore) {


   suspend fun saveLastSearchCity(city: String){
        weatherDataStore.saveLastSearchedCity(city)
    }

    suspend fun getLastSearchCity() = weatherDataStore.getLastSearchedCity()

}

package com.example.weatherapptechscreen.repository

import com.example.weatherapptechscreen.data.model.WeatherResponse
import com.example.weatherapptechscreen.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 * Copyright © 2023 Kaiser Permanente. All rights reserved.
 */
interface WeatherRepository {

    suspend fun saveLastSearchCity(cityName: String)
    suspend fun getLastSearchCity(): String
    suspend fun getWeatherByCoordinates(
        lat: Double,
        long: Double
    ): Flow<NetworkResult<WeatherResponse>>

    suspend fun getWeatherByCityName(cityName: String): Flow<NetworkResult<WeatherResponse>>

}

package com.example.weatherapptechscreen.data.remote

import com.example.weatherapptechscreen.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 */
interface WeatherAPI {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): WeatherResponse

    companion object {
        const val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
    }
}

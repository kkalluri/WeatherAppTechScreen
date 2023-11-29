package com.example.weatherapptechscreen.data.model

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 */

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String,
    // Add other properties as needed
)

data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    // Add other properties as needed
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
    // Add other properties as needed
)

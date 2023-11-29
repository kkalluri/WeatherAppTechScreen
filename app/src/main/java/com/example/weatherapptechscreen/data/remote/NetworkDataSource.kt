package com.example.weatherapptechscreen.data.remote

import android.util.Log
import com.example.weatherapptechscreen.data.model.WeatherResponse
import com.example.weatherapptechscreen.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import com.example.weatherapptechscreen.BuildConfig

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 */
class NetworkDataSource @Inject constructor(
    private val weatherAPI: WeatherAPI,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
     fun getWeatherByCoordinates(lat:Double, long: Double): Flow<NetworkResult<WeatherResponse>> = flow {
        try {
            emit(NetworkResult.Loading(true))

            val response = weatherAPI.getWeatherByCoordinates(lat,long, BuildConfig.WEATHER_API_KEY )

            Log.d("NetworkDataSource getWeatherByCoordinates", "response $response")
            emit(NetworkResult.Success(response))
        } catch (e: Exception) {
            // Handle the network error and emit a Failure
            emit(NetworkResult.Failure("Failed to fetch weather"))
        }
    }.flowOn(dispatcher)


     fun getWeatherByCity(city: String):Flow<NetworkResult<WeatherResponse>> = flow {
        try {
            emit(NetworkResult.Loading(true))

            val response = weatherAPI.getWeatherByCity(city, BuildConfig.WEATHER_API_KEY)

            Log.d("NetworkDataSource getWeatherByCity", "response $response")
            emit(NetworkResult.Success(response))
        } catch (e: Exception) {
            // Handle the network error and emit a Failure
            emit(NetworkResult.Failure("Failed to fetch weather"))
        }
    }.flowOn(dispatcher)

}

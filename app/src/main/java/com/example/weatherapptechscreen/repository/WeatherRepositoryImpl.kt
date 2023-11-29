package com.example.weatherapptechscreen.repository


import com.example.weatherapptechscreen.data.local.LocalDataSource
import com.example.weatherapptechscreen.data.model.WeatherResponse
import com.example.weatherapptechscreen.data.remote.NetworkDataSource
import com.example.weatherapptechscreen.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 */
class WeatherRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
): WeatherRepository {


   override suspend fun saveLastSearchCity(cityName: String){
        localDataSource.saveLastSearchCity(cityName)
    }

    override suspend fun getLastSearchCity(): String {
        println("WeatherRepositoryImpl ${localDataSource.getLastSearchCity()}")
       return localDataSource.getLastSearchCity()
    }

    override suspend fun getWeatherByCoordinates(lat: Double, long: Double): Flow<NetworkResult<WeatherResponse>> {
        return networkDataSource.getWeatherByCoordinates(lat, long)
    }

    override suspend fun getWeatherByCityName(cityName: String): Flow<NetworkResult<WeatherResponse>> {
      return networkDataSource.getWeatherByCity(cityName)
    }


}

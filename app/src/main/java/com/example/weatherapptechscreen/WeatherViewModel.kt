package com.example.weatherapptechscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptechscreen.data.model.WeatherResponse
import com.example.weatherapptechscreen.data.remote.NetworkResult
import com.example.weatherapptechscreen.repository.WeatherRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 *
 * WeatherViewModel class serves as the ViewModel for handling and managing weather-related data
 * within the application and manage ui state
 */
class WeatherViewModel @Inject constructor(private val repository: WeatherRepositoryImpl) :
    ViewModel() {


    private val _enteredCity = MutableLiveData<String>()
    val enteredCity: LiveData<String> get() = _enteredCity

    private var updatedCity: String? = null

    fun getCity() = updatedCity

    fun updateEnteredCity(city: String) {
        _enteredCity.value = city
    }

    private val _latLong = MutableLiveData<String>()
    val latLong: LiveData<String> get() = _latLong

    fun updateLatLong(latlong: String) {
        _latLong.value = latlong
    }

    // to track whether the last searched city has been loaded
    private val _lastSearchedCityLoaded = MutableLiveData<Boolean>()
    val lastSearchedCityLoaded: LiveData<Boolean> get() = _lastSearchedCityLoaded

    // hold the network result of weather
    private val _weatherResponse = MutableStateFlow<NetworkResult<WeatherResponse>>(
        NetworkResult.Loading(false)
    )
    val weatherResponse = _weatherResponse.asStateFlow()


    // Function to fetch weather information by city name
    fun getWeatherByCity(city: String) {
        println("getWeatherByCity called : $city")
        viewModelScope.launch {
            repository.getWeatherByCityName(city).collect { networkResult ->
                _weatherResponse.value = networkResult
            }
        }
    }

    // Function to fetch weather information by coordinates (latitude, longitude)
    fun getWeatherByCoordinates(lat: Double, lon: Double) {
        println("getCityByCoordinates called : $lat,$lon ")
        viewModelScope.launch {
            repository.getWeatherByCoordinates(lat, lon).collect { networkResult ->
                _weatherResponse.value = networkResult
            }
        }
    }

    // Function to fetch the last searched city from the repository (data store)
    fun getLastSearchedCity() {
        println("view model getLastSearchedCity called ")
        viewModelScope.launch {
            var city = repository.getLastSearchCity()
            println("after response  view model getLastSearchedCity : $city")

            updatedCity = city
            _lastSearchedCityLoaded.value = true
            _enteredCity.value = city
        }
    }

    // Function to save the last searched city to the repository(data store)
    fun saveLastSearchedCity(city: String) {
        viewModelScope.launch {
            repository.saveLastSearchCity(city)
        }
    }
}


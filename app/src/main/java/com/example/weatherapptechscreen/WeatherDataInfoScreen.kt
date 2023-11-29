package com.example.weatherapptechscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapptechscreen.data.model.WeatherResponse
import com.example.weatherapptechscreen.data.remote.NetworkResult

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/29/23.
 */

@Composable
fun WeatherInformation(viewModel: WeatherViewModel, fetchWeatherDataByCity: (String) -> Unit , fetchWeatherDataByCoordinates: (String) -> Unit) {

    val enteredCity by viewModel.enteredCity.observeAsState()
    val latlang by viewModel.latLong.observeAsState()

    val weatherResponseResult = viewModel.weatherResponse.collectAsState().value


    // Trigger data fetching when the entered city/ saved city
    LaunchedEffect(enteredCity) {
        println("LaunchedEffect triggered enteredCity with value: ${viewModel.enteredCity.value}")
        viewModel.enteredCity.value?.let { selectedItem ->
            if(!selectedItem.isNullOrBlank()) {
                fetchWeatherDataByCity(selectedItem)
            }
        }
    }
    // Trigger data fetching when the latlang is there
    LaunchedEffect(latlang) {
        println("LaunchedEffect triggered latlang with value: ${viewModel.latLong.value}")
        viewModel.latLong.value?.let { selectedItem ->
            fetchWeatherDataByCoordinates(selectedItem)
        }
    }

    when (weatherResponseResult) {
        is NetworkResult.Loading -> {
            LoadingBar()
        }

        is NetworkResult.Success -> {
            var response = (weatherResponseResult as NetworkResult.Success<WeatherResponse>).data
            viewModel.saveLastSearchedCity(response.name)
            Column(
                Modifier
                    .padding(20.dp)
            ) {
                WeatherInfo(response)
            }
        }

        is NetworkResult.Failure -> {
            var errorMessage =
                (weatherResponseResult as NetworkResult.Failure<WeatherResponse>).errorMessage
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(10.dp))
        }
    }

}


@Composable
fun WeatherInfo(weatherData: WeatherResponse?) {
    if (weatherData != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Display weather information, e.g., temperature, description
            Text("City Name: ${weatherData.name}")
            Text("Temperature: ${weatherData.main.temp} °C")
            Text("Description: ${weatherData.weather.firstOrNull()?.description}")

            // Weather Icon
            WeatherIcon(iconUrl = "https://openweathermap.org/img/w/${weatherData.weather.firstOrNull()?.icon}.png")
        }
    }
}

@Composable
fun WeatherIcon(iconUrl: String) {
    AsyncImage(
        model = iconUrl,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .width(50.dp)
                .height(4.dp)
        )
    }
}

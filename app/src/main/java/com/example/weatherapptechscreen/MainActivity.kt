package com.example.weatherapptechscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapptechscreen.features.SearchScreen
import com.example.weatherapptechscreen.features.WeatherInformation
import com.example.weatherapptechscreen.features.WeatherViewModel
import com.example.weatherapptechscreen.ui.theme.WeatherAppTechScreenTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTechScreenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(modifier = Modifier) {

                        SearchScreen(viewModel = viewModel)
                        WeatherInformation(viewModel = viewModel,
                            fetchWeatherDataByCity = { enteredCity ->
                                viewModel.getWeatherByCity(enteredCity)
                            },
                            fetchWeatherDataByCoordinates = {
                                var latlonData = it.split(",")
                                viewModel.getWeatherByCoordinates(
                                    latlonData[0].toDouble(),
                                    latlonData[1].toDouble()
                                )
                            })
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTechScreenTheme {
        //Greeting("Android")
    }
}

package com.example.weatherapptechscreen.features

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 */
// Composable function for the main search screen
@Composable
fun SearchScreen(
    viewModel: WeatherViewModel
) {
    // State variables to track the entered city name and whether to show the empty city name dialog
    var cityName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    // Observe changes in lastSearchedCityLoaded
    val lastSearchedCityLoaded by viewModel.lastSearchedCityLoaded.observeAsState()


    LaunchedEffect(true) {
        viewModel.getLastSearchedCity()
    }


    if (lastSearchedCityLoaded == true) {
        if (cityName.isEmpty() && showDialog) {
            EmptyCityNameDialog(onDismissRequest = {
                showDialog = false
            })
        }
        // Request location permission
        if (viewModel.getCity()?.isEmpty() == true) {
            LocationPermissionHandler {
                println("LocationPermissionHandler  ${it.latitude},${it.longitude} ")
                viewModel.updateLatLong("${it.latitude},${it.longitude}")
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // City Input
        CityInput(cityName = cityName) {
            cityName = it
        }
        // Search Button
        SearchButton(
            onClick = {
                if (cityName.isNotEmpty()) {
                    viewModel.updateEnteredCity(cityName)
                } else {
                    showDialog = true
                }
                cityName = ""
            }
        )

    }
}

// Composable function for the city input field
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityInput(cityName: String, onCityNameChanged: (String) -> Unit) {
    TextField(
        value = cityName,
        onValueChange = { onCityNameChanged.invoke(it) },
        label = { Text("Enter City") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    )
}

// Composable function for the search button
@Composable
fun SearchButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("Search")
    }
}

// Composable function to handle location permission

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(onLocationPermissionGranted: (Location) -> Unit) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)


    // TODO: handle permission not granted

    if (permissionState.status.isGranted) {
        // Permission is already granted, get the location
        val location = getLastKnownLocation(context)
        if (location != null) {
            onLocationPermissionGranted(location)
        }
    } else {
        // Request permission if not granted
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }
}

// Function to get the last known location
@Composable
fun getLastKnownLocation(context: Context): Location? {
    try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if location providers are enabled
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            // Get the last known location from any available provider
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            // Return the more accurate of the two locations
            return if (gpsLocation != null && (networkLocation == null || gpsLocation.accuracy < networkLocation.accuracy)) {
                gpsLocation
            } else {
                networkLocation
            }
        }
    } catch (e: SecurityException) {
        Log.e("LocationPermission", "Error getting location: ${e.message}")
    }
    return null
}

// Composable function for the dialog displayed when the city name is empty
@Composable
fun EmptyCityNameDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("Error", color = MaterialTheme.colorScheme.error)
        },
        text = {
            Text("City name cannot be empty.")
        },

        confirmButton = {
            Button(
                onClick = { onDismissRequest() }
            ) {
                Text("OK")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

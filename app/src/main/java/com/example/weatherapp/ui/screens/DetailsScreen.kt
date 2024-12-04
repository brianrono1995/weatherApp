package com.example.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // For layouts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.ui.viewmodel.WeatherViewModel

@Composable
fun DetailsScreen(
    date: String,
    city: String,
    viewModel: WeatherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(city, date) {
        viewModel.fetchWeather(city, date, BuildConfig.WEATHER_API_KEY)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 8.dp) // Increased margin from top
                .background(MaterialTheme.colorScheme.primary)
        )
        Text(
            text = "Weather Details for on $date",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Text("Loading...", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            uiState.error != null -> {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(uiState.hourlyWeather) { hourlyWeather ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Time: ${hourlyWeather.time}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Temperature: ${hourlyWeather.temp_c}°C",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Humidity: ${hourlyWeather.humidity}%",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Wind Speed: ${hourlyWeather.wind_kph} km/h",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Condition: ${hourlyWeather.condition.text}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

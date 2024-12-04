package com.example.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WeatherViewModel,
    onDateSelected: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // State for DatePicker
    val datePickerState = rememberDatePickerState()
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE)) }

    // State for city selection
    var city by remember { mutableStateOf("Nairobi") }

    LaunchedEffect(city, selectedDate) {
        viewModel.fetchWeather(city, selectedDate, com.example.weatherapp.BuildConfig.WEATHER_API_KEY)
    }

    // Scroll state for the screen
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB)) // Light blue sky color
    ) {
        // Fixed Title Section (Non-scrollable)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 8.dp) // Increased margin from top
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(

                text = "Weather Forecast",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // City Input Field
            TextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Enter City") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date Selection Button
            Button(
                onClick = { isDatePickerVisible = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Selected Date: $selectedDate")
            }

            if (isDatePickerVisible) {
                DatePickerDialog(
                    onDismissRequest = { isDatePickerVisible = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    val date = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                    selectedDate = date.format(DateTimeFormatter.ISO_DATE)
                                    onDateSelected(selectedDate, city)
                                }
                                isDatePickerVisible = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isDatePickerVisible = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weather Display
            when {
                uiState.isLoading -> {
                    Text(
                        "Loading...",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                else -> {
                    uiState.hourlyWeather.forEach { hourlyWeather ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = hourlyWeather.time,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${hourlyWeather.temp_c}°C | ${hourlyWeather.temp_c * 9 / 5 + 32}°F",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

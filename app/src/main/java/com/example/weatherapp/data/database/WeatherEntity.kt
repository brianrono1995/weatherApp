package com.example.weatherapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapp.data.api.HourlyWeather

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val date: String, // The date for which the data is stored
    @TypeConverters(Converters::class) val hourlyWeather: List<HourlyWeather> // List of hourly weather data
)

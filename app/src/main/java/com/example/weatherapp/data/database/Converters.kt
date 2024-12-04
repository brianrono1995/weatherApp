package com.example.weatherapp.data.database

import androidx.room.TypeConverter
import com.example.weatherapp.data.api.HourlyWeather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromHourlyWeatherList(value: List<HourlyWeather>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toHourlyWeatherList(value: String): List<HourlyWeather> {
        val listType = object : TypeToken<List<HourlyWeather>>() {}.type
        return gson.fromJson(value, listType)
    }
}

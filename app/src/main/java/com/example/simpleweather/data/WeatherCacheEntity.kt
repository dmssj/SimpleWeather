package com.example.simpleweather.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val city: String,
    val country: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val icon: String
)
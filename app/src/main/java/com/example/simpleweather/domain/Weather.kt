package com.example.simpleweather.domain

data class Weather(
    val city: String,
    val country: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val iconUrl: String
) {
    val iconFullUrl: String
        get() = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
}
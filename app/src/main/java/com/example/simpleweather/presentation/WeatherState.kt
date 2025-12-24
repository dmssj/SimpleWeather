package com.example.simpleweather.presentation

import com.example.simpleweather.domain.Weather

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weather: Weather) : WeatherState()
    data class Error(val message: String) : WeatherState()
    object Empty : WeatherState()
}
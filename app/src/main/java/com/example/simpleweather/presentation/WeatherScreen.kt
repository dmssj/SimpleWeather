package com.example.simpleweather.presentation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.simpleweather.data.CityEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = viewModel()
    val context = LocalContext.current

    val state by viewModel.state.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val filteredCities = viewModel.getFilteredCities()

    // Загружаем города один раз
    LaunchedEffect(Unit) {
        viewModel.setCityList(loadCitiesFromAssets(context))
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Погода",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { viewModel.onSearchTextChange(it) },
            label = { Text("Введите город") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            )
        )

        // Список автокомплита
        if (filteredCities.isNotEmpty()) {
            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                items(filteredCities) { city ->
                    Text(
                        text = city.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onSearchTextChange(city.name)
                                viewModel.loadWeather(city.name)
                            }
                            .padding(8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is WeatherState.Empty -> Text("Введите город")
            is WeatherState.Loading -> {
                CircularProgressIndicator()
                Text("Загрузка...", modifier = Modifier.padding(top = 16.dp))
            }
            is WeatherState.Success -> {
                val weather = (state as WeatherState.Success).weather
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "${weather.city}, ${weather.country}",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        AsyncImage(
                            model = weather.iconFullUrl,
                            contentDescription = "Погода",
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            "${weather.temperature.toInt()}°C",
                            style = MaterialTheme.typography.displaySmall
                        )
                        Text(weather.description, style = MaterialTheme.typography.titleLarge)
                        Text("Влажность: ${weather.humidity}%", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            is WeatherState.Error -> {
                Text(
                    "Ошибка: ${(state as WeatherState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// Загрузка городов из assets/cities.json
fun loadCitiesFromAssets(context: Context): List<CityEntity> {
    return try {
        val inputStream = context.assets.open("cities.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<CityEntity>>() {}.type
        Gson().fromJson<List<CityEntity>>(reader, type)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
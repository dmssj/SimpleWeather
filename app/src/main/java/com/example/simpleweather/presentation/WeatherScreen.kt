package com.example.simpleweather.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = viewModel()


    val state by viewModel.state.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Погода",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange,
            label = { Text("Город") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.loadWeather()
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.loadWeather() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Узнать погоду")
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (state) {
            is WeatherState.Loading -> {
                CircularProgressIndicator()
                Text("Загрузка...", modifier = Modifier.padding(top = 16.dp))
            }

            is WeatherState.Success -> {
                val weather = (state as WeatherState.Success).weather

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${weather.city}, ${weather.country}",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        AsyncImage(
                            model = weather.iconFullUrl,
                            contentDescription = "Погода",
                            modifier = Modifier.size(100.dp)
                        )

                        Text(
                            text = "${weather.temperature.toInt()}°C",
                            style = MaterialTheme.typography.displaySmall
                        )


                        Text(
                            text = weather.description.replaceFirstChar { char -> char.uppercaseChar() },
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Влажность: ${weather.humidity}%",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            is WeatherState.Error -> {
                val error = (state as WeatherState.Error).message
                Text(
                    text = "Ошибка: $error",
                    color = MaterialTheme.colorScheme.error
                )
                Button(
                    onClick = { viewModel.loadWeather() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Попробовать снова")
                }
            }

            WeatherState.Empty -> {
                Text("Введите город")
            }
        }
    }
}
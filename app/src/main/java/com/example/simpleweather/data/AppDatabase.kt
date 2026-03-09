package com.example.simpleweather.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CityEntity::class, WeatherCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun weatherDao(): WeatherDao
}
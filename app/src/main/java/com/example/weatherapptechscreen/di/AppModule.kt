package com.example.weatherapptechscreen.di

import android.content.Context
import com.example.weatherapptechscreen.data.local.WeatherDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/28/23.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherDataStore(@ApplicationContext context: Context): WeatherDataStore {
        return WeatherDataStore(context)
    }
}

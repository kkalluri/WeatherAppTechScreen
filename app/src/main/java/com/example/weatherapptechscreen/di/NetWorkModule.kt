package com.example.weatherapptechscreen.di

import com.example.weatherapptechscreen.data.remote.WeatherAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 *
 * WeatherAppTechScreen
 * Created by venkatakalluri on 11/21/23.
 */
@Module
@InstallIn(SingletonComponent::class)
class NetWorkModule {


    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient.Builder().addInterceptor(interceptor)
            .build()


    @Singleton
    @Provides
    fun provideRetrofitService(okHttpClient: OkHttpClient): WeatherAPI =
        Retrofit.Builder()
            .baseUrl(WeatherAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WeatherAPI::class.java)

}



package com.udacity.asteroidradar.api


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitAsteroidList = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

private val retrofitNASAPictureOfDay = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()


interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAllAsteroids(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String
    ): String
    //@Query("end_date") endDate: String, --> Default is always 7 days so there is no need to apply it

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("api_key") apiKey: String
    ): PictureOfDay
}

object NasaApi {
    val retrofitAsteroidsService: NasaApiService by lazy {
        retrofitAsteroidList.create(NasaApiService::class.java)
    }
    val retrofitPictureService: NasaApiService by lazy {
        retrofitNASAPictureOfDay.create(NasaApiService::class.java)
    }
}
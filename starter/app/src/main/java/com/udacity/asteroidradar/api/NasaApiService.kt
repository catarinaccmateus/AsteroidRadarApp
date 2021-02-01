package com.udacity.asteroidradar.api


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.data.NetworkPictureOfDay
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.data.PictureOfDay
import kotlinx.coroutines.Deferred
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
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

private val retrofitNASAPictureOfDay = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()


interface NasaApiService {
    @GET("neo/rest/v1/feed")
    fun getAllAsteroids(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String
    ): Deferred<String>
    // The default end_date for the query is 7 days so there is no need to apply the Constants.DEFAULT_END_DATE_DAYS

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("api_key") apiKey: String
    ): NetworkPictureOfDay
}

object NasaApi {
    val retrofitAsteroidsService: NasaApiService by lazy {
        retrofitAsteroidList.create(NasaApiService::class.java)
    }
    val retrofitPictureService: NasaApiService by lazy {
        retrofitNASAPictureOfDay.create(NasaApiService::class.java)
    }

}
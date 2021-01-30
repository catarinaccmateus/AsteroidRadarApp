package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

const val APIKey = "eBwI2e5kcZEfHxaJRRhvo6kxrgBoeiyciCndnHRg"

const val APIKEY = "api_key=$APIKey"

const val baseURL = "https://api.nasa.gov/"

const val baseURLImage = "https://api.nasa.gov/planetary/apod/"


private val retrofitAsteroidList = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(baseURL)
    .build()

private val retrofitImageOfTheDay = Retrofit.Builder()
    .baseUrl(baseURLImage)
    .build()

interface NasaApiService {
    @GET("neo/rest/v1/feed")
    fun getAllAsteroids(
        @Query("start_date") startDate: String,
        //@Query("end_date") endDate: String,
        @Query("api_key") apiKey: String): Call<String>
}

object NasaApi {
    val retrofitService: NasaApiService by lazy {
        retrofitAsteroidList.create(NasaApiService::class.java)
    }
}
package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.api.asDomainModel
import com.udacity.asteroidradar.getCurrentDateAsString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

enum class AsteroidApiStatus { LOADING, ERROR, DONE }


class AsteroidsRepository(private val database: AsteroidsDatabase) {
    /**
     * Getting Asteroids from database and converting it to domain model.
     * */
    val asteroids: LiveData<ArrayList<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroidsFromToday()) {
            it.asDomainModel()
        }

    /** API Request Status */
    var apiStatus: AsteroidApiStatus = AsteroidApiStatus.DONE

    /**
     * Getting Asteroids from network and converting it to database model.
     * */
    suspend fun refreshAsteroids() {
        apiStatus = AsteroidApiStatus.LOADING
        withContext(Dispatchers.IO) {
            try {
                val response = NasaApi.retrofitAsteroidsService.getAllAsteroids(
                    getCurrentDateAsString(),
                    Constants.APIKey
                ).await()
                /**
                 * Personal note: Async functions returns a Deferred — a light-weight non-blocking future that represents a promise to provide a result later.
                 * You can use .await() on a deferred value to get its eventual result.
                 * */
                val jsonResponse = JSONObject(response)
                val data = parseAsteroidsJsonResult(jsonResponse)
                /**
                 * Personal note: "*" can be applied to an Array before passing it into a function that accepts varargs.
                 * */
                database.asteroidDao.instertAll(*NetworkAsteroidContainer(data).asDatabaseModel())
                apiStatus = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                apiStatus = AsteroidApiStatus.ERROR
                e.printStackTrace()
            }

        }
    }
}
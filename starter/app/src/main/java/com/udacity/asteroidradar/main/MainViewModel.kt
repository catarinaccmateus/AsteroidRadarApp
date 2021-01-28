package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

/* For testing purposes */
val listOfAsteroids: List<Asteroid> = listOf(
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, false),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true)
    )


class MainViewModel : ViewModel() {
    val data = MutableLiveData<List<Asteroid>?>()

    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = _navigateToAsteroidDetail

    init {
        data.value = listOfAsteroids
    }

    fun onAsteroidItemClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetail.value = null
    }
}
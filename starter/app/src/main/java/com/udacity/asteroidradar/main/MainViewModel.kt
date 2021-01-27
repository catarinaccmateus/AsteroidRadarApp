package com.udacity.asteroidradar.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

/* For testing purposes */
val listOfAsteroids: List<Asteroid> = listOf(
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true)
    )


class MainViewModel : ViewModel() {
    val data = MutableLiveData<List<Asteroid>?>()

    init {
        data.value = listOfAsteroids
    }
}
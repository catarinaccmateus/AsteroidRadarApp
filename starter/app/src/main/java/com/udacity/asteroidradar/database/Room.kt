package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidsDao {
    @Query("select * from databaseasteroid")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun instertAll(vararg asteroids: DatabaseAsteroid)
    @Query("select * from databaseasteroid where closeApproachDate >= date('now') order by closeApproachDate asc ")
    fun getAllAsteroidsFromToday(): LiveData<List<DatabaseAsteroid>>
}
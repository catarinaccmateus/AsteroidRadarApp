package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.worker.DeletePreviousDataWorker
import com.udacity.asteroidradar.worker.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumesAll
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val contraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val fetchDataRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(contraints)
            .addTag(RefreshDataWorker.WORK_NAME)
            .build()


        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                RefreshDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                fetchDataRequest)
    }

    /** Help - How te implement multiple periodic work requests in the same workManager?
     *
     *         Second PeriodicWorkRequest:
     *
     *         val deleteDataRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<DeletePreviousDataWorker>(1, TimeUnit.DAYS)
     *          .setConstraints(contraints)
     *          .addTag(DeletePreviousDataWorker.WORK_NAME)
     *          .build()
     *
     *           Tried this but not sure if it would work:
     *
     *           WorkManager.getInstance()
     *           .enqueue(listOf(fetchDataRequest, deleteDataRequest))
     *
     * */



}
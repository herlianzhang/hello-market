package com.dpr.hello_market.repository

import com.dpr.hello_market.db.activity.ActivityDao
import com.dpr.hello_market.db.activity.ActivityDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActivityRepository @Inject constructor(private val dao: ActivityDao) {
    val activityList = dao.getActivity()

    suspend fun addActivity(activity: ActivityDbModel) {
        withContext(Dispatchers.IO) {
            dao.insertActivity(activity)
        }
    }
}
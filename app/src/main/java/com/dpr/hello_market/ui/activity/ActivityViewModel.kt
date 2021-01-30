package com.dpr.hello_market.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dpr.hello_market.repository.ActivityRepository
import javax.inject.Inject

class ActivityViewModel @Inject constructor(app: Application, private val activityRepository: ActivityRepository) : AndroidViewModel(app) {
    val activity = activityRepository.activityList
}
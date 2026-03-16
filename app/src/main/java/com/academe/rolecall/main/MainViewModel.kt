package com.academe.rolecall.main

import androidx.lifecycle.ViewModel
import com.academe.rolecall.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    suspend fun getNextDestination(): String {
        return when {
            preferences.authenticatedFlow.first() -> Screens.Dashboard.name
            else -> Screens.Login.name
        }
    }
}
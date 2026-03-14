package com.academe.rolecall.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academe.rolecall.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    private val _currentRoute = MutableStateFlow(Screens.Login.name)
    val currentRoute = _currentRoute.asStateFlow()

    init {
        viewModelScope.launch {
            _currentRoute.value = when {
                preferences.authenticatedFlow.first() && preferences.demoModeFlow.first() -> Screens.Dashboard.name
                else -> Screens.Login.name
            }
        }
    }
}
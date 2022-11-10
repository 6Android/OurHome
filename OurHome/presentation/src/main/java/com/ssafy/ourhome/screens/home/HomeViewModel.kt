package com.ssafy.ourhome.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.usecase.user.EditLocationPermissionUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val editLocationPermissionUseCase: EditLocationPermissionUseCase
): ViewModel() {

    fun editLocationPermission(permission: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        editLocationPermissionUseCase.execute(Prefs.familyCode, Prefs.email, permission).collect { response ->
            when (response) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    Log.d("test5", "editLocationPermission: 잘 됐음.")
                }
                is ResultType.Error -> {
                    Log.d("test5", "editLocationPermission: 에러낫음!!!!!!.")
                }
                else -> {

                }
            }
        }
    }
}
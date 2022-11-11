package com.ssafy.ourhome.screens.userpage.setting

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
class SettingViewModel @Inject constructor(
    private val editLocationPermissionUseCase: EditLocationPermissionUseCase
) : ViewModel() {

    fun editLocationPermission(permit : Boolean){
        viewModelScope.launch(Dispatchers.IO) {

            editLocationPermissionUseCase.execute(Prefs.familyCode,Prefs.email,permit).collect{
                when(it){
                    is ResultType.Success -> {
                        Log.d("SettingViewModel", "editLocationPermissionSuccess: ")
                    }
                    else ->{
                        Log.d("SettingViewModel", "editLocationPermissionError: ")
                    }
                }

            }
        }

    }

}
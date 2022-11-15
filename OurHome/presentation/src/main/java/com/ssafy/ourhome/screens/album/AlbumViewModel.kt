package com.ssafy.ourhome.screens.album

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.data.utils.EMAIL
import com.ssafy.domain.model.album.DomainAlbumDTO
import com.ssafy.domain.repository.album.AlbumRepository
import com.ssafy.domain.usecase.album.UploadAlbumUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val uploadAlbumUseCase: UploadAlbumUseCase
) : ViewModel() {

    var uploadSuccess by mutableStateOf(false)
        private set

    fun uploadAlbum(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val today = LocalDateTime.now()

            uploadAlbumUseCase.execute(
                Prefs.familyCode, imageUri,
                DomainAlbumDTO(
                    Prefs.email,
                    "",
                    today.toString(),
                    today.year,
                    today.monthValue,
                    today.dayOfMonth,
                    today.hour,
                    today.minute,
                    "${today.year}년 ${today.monthValue}월"

                )

            ).collect {

                if (it is ResultType.Success) {
                    uploadSuccess = true
                }
            }
        }
    }

    fun setUploadSuccess(){
        this.uploadSuccess = false
    }
}
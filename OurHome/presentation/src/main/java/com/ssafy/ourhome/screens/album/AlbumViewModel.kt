package com.ssafy.ourhome.screens.album

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.album.DomainAlbumDTO
import com.ssafy.domain.usecase.album.DeleteAlbumImageUseCase
import com.ssafy.domain.usecase.album.GetAlbumImagesUseCase
import com.ssafy.domain.usecase.album.UploadAlbumUseCase
import com.ssafy.domain.usecase.family.GetFamilyManagerUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val uploadAlbumUseCase: UploadAlbumUseCase,
    private val getAlbumImagesUseCase: GetAlbumImagesUseCase,
    private val deleteAlbumImageUseCase: DeleteAlbumImageUseCase,
    private val getFamilyManagerUseCase: GetFamilyManagerUseCase
) : ViewModel() {

    var uploadSuccess by mutableStateOf(false)
        private set

    var albumImages by mutableStateOf(mapOf<String, List<DomainAlbumDTO>>())
        private set

    var getAlbumImagesProcessState = mutableStateOf(State.DEFAULT)

    var deleteAlbumImagesProcessState by mutableStateOf(State.DEFAULT)

    var visibleDeleteIconState = mutableStateOf(false)

    var albumDetail by mutableStateOf(DomainAlbumDTO())

    fun initAlbumDetail(){
        if(albumDetail.email == Prefs.email){
            visibleDeleteIconState.value = true
        }
    }

    fun getFamilyManager() = viewModelScope.launch(Dispatchers.IO) {
        getFamilyManagerUseCase.execute(Prefs.familyCode).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    if(!it.data.isNullOrEmpty()){
                        visibleDeleteIconState.value = true
                    }
                }
                is ResultType.Error -> {

                }
            }
        }
    }

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

    fun getAlbumImages()= viewModelScope.launch(Dispatchers.IO) {
        getAlbumImagesUseCase.execute(Prefs.familyCode).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    albumImages = it.data.groupBy {
                        it.mapping_date
                    }
                    getAlbumImagesProcessState.value = State.SUCCESS
                }
                is ResultType.Error -> {
                    getAlbumImagesProcessState.value = State.ERROR
                }
            }
        }
    }

    fun deleteAlbumImage() = viewModelScope.launch(Dispatchers.IO) {
        deleteAlbumImageUseCase.execute(Prefs.familyCode, albumDetail.date).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    deleteAlbumImagesProcessState = State.SUCCESS
                }
                is ResultType.Error -> {
                    deleteAlbumImagesProcessState = State.ERROR
                }
            }
        }
    }
}
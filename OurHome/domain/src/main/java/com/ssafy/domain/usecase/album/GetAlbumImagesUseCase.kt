package com.ssafy.domain.usecase.album

import android.net.Uri
import com.ssafy.domain.model.album.DomainAlbumDTO
import com.ssafy.domain.repository.album.AlbumImages
import com.ssafy.domain.repository.album.AlbumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAlbumImagesUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
){
    fun execute(familyCode: String) =
        albumRepository.getAlbumImages(familyCode)
}
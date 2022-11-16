package com.ssafy.domain.usecase.album

import com.ssafy.domain.repository.album.AlbumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteAlbumImageUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
){
    fun execute(familyCode: String, documentCode: String) =
        albumRepository.deleteAlbumImage(familyCode, documentCode)
}
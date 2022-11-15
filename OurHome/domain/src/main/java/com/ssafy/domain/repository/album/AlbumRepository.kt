package com.ssafy.domain.repository.album

import android.net.Uri
import com.ssafy.domain.model.album.DomainAlbumDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    // 앨범 정보 추가하기
    fun uploadAlbum(familyCode : String, imageUri: Uri, album: DomainAlbumDTO): Flow<ResultType<Unit>>
}
package com.ssafy.domain.repository.album

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.ssafy.domain.model.album.DomainAlbumDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias AlbumImages = List<DomainAlbumDTO>
typealias AlbumResponse = ResultType<AlbumImages>

interface AlbumRepository {

    // 앨범 정보 추가하기
    fun uploadAlbum(familyCode : String, imageUri: Uri, album: DomainAlbumDTO): Flow<ResultType<Unit>>

    // 앨범 이미지 가져오기
    fun getAlbumImages(familyCode: String): Flow<AlbumResponse>

    fun deleteAlbumImage(familyCode: String, documentCode: String) : Flow<ResultType<Unit>>
}
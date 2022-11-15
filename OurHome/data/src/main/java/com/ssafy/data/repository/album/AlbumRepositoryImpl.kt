package com.ssafy.data.repository.album

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.datasource.album.AlbumDataSource
import com.ssafy.domain.model.album.DomainAlbumDTO
import com.ssafy.domain.repository.album.AlbumRepository
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumDataSource: AlbumDataSource
) : AlbumRepository {

    override fun uploadAlbum(
        familyCode: String,
        imageUri: Uri,
        album: DomainAlbumDTO
    ): Flow<ResultType<Unit>> =
        callbackFlow {
            albumDataSource.uploadAlbumImage(imageUri).addOnSuccessListener {
                it.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    Log.d("test5", "uploadAlbumInfo: $uri")
                    albumDataSource.uploadAlbumInfo(
                        familyCode,
                        album.copy(imageUri = uri.toString())
                    ).addOnCompleteListener {
                        Log.d("test5", "successWithImage")
                        trySend(ResultType.Success(Unit))
                    }
                }
            }
            awaitClose {  }
        }

}
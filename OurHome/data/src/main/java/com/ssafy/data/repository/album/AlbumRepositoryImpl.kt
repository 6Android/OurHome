package com.ssafy.data.repository.album

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.datasource.album.AlbumDataSource
import com.ssafy.domain.model.album.DomainAlbumDTO
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.repository.album.AlbumRepository
import com.ssafy.domain.repository.album.AlbumResponse
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

    override fun getAlbumImages(familyCode: String): Flow<AlbumResponse> =
        callbackFlow {
            val snapshotListener =
                albumDataSource.getAlbumImages(familyCode).addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val albumImages = snapshot.toObjects(DomainAlbumDTO::class.java)
                        ResultType.Success(albumImages)
                    } else {
                        ResultType.Error(e)
                    }
                    trySend(response)
                }
            awaitClose {
                snapshotListener.remove()
            }
        }

    override fun deleteAlbumImage(
        familyCode: String,
        documentCode: String
    ): Flow<ResultType<Unit>> =
        callbackFlow {
            albumDataSource.deleteAlbumImage(familyCode, documentCode).addOnCompleteListener {
                if(it.isSuccessful){
                    trySend(ResultType.Success(Unit))
                }else{
                    trySend(ResultType.Error(it.exception))
                }
            }.addOnFailureListener {
                trySend(ResultType.Error(it))
            }
            awaitClose {  }
        }
}
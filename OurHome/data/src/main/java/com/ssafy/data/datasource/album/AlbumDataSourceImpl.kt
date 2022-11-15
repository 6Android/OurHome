package com.ssafy.data.datasource.album

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.ssafy.data.utils.ALBUM
import com.ssafy.data.utils.ALBUM_IMAGE
import com.ssafy.data.utils.FAMILY
import com.ssafy.data.utils.PROFILE_IMAGE
import com.ssafy.domain.model.album.DomainAlbumDTO
import java.time.LocalDateTime
import javax.inject.Inject

class AlbumDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : AlbumDataSource {

    override fun uploadAlbumInfo(familyCode: String, albumDto: DomainAlbumDTO) =
        fireStore.collection(FAMILY).document(familyCode).collection(ALBUM).document(albumDto.date)
            .set(albumDto)


    override fun uploadAlbumImage(imageUri: Uri): UploadTask {
        val storageRef =
            firebaseStorage.reference.child(ALBUM_IMAGE)
                .child(LocalDateTime.now().toString())
        return storageRef.putFile(imageUri)
    }
}
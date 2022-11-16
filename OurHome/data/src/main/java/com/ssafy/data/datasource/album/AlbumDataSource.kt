package com.ssafy.data.datasource.album

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.storage.UploadTask
import com.ssafy.domain.model.album.DomainAlbumDTO

interface AlbumDataSource {

    fun uploadAlbumInfo(familyCode : String, albumDto : DomainAlbumDTO) : Task<Void>

    fun uploadAlbumImage(imageUri : Uri) : UploadTask

    fun getAlbumImages(familyCode: String) : Query

    fun deleteAlbumImage(familyCode: String, documentCode: String) : Task<Void>
}
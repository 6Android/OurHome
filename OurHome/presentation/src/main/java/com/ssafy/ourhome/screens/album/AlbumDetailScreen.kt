package com.ssafy.ourhome.screens.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun AlbumDetailScreen(navController : NavController, photoUrl: String, photoDate: String){
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "AlbumDetailScreen")
        Text(text = "$photoUrl")

        val painter =
            rememberAsyncImagePainter(photoUrl)

        Image(
            modifier = Modifier.size(250.dp),
            painter = painter,
            contentDescription = "펫 이미지"
        )

        Text(text = "$photoDate")
    }

}
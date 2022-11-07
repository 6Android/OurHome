package com.ssafy.ourhome.screens.album

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AlbumDetailScreen(navController : NavController, photoUrl: String, photoDate: String){
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false // Status bar 안보이기

    val painter = rememberAsyncImagePainter(photoUrl)

    var isVisible by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .clickable {
                isVisible = isVisible.not()
            }
    ){
        AlbumImage(painter)

        AlbumTopBar(isVisible, navController, photoDate)
    }
}

/** 엘범 탑 바 **/
@Composable
private fun AlbumTopBar(
    isVisible : Boolean,
    navController: NavController,
    photoDate: String
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = {
            -it
        }),
        exit = slideOutVertically(targetOffsetY = {
            -it
        })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .background(color = Color(0x55000000)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            /** 뒤로가기 **/
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "BackButton",
                modifier = Modifier.clickable {
                    navController.popBackStack()
                },
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(12.dp))

            /** 이미지 등록 날짜 **/
            Text(
                text = "$photoDate",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
            )

            /** 휴지통 아이콘 **/
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable { // 사진 삭제

                    },
                    tint = Color.White
                )
            }
        }
    }

}

/** 엘범 이미지 **/
@Composable
private fun AlbumImage(painter: AsyncImagePainter) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = "펫 이미지",
            contentScale = ContentScale.Fit
        )
    }
}
package com.ssafy.ourhome.screens.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.question.navigateChatScreen
import com.ssafy.ourhome.utils.CHATTING_ICON_BLACK
import okhttp3.internal.checkOffsetAndCount
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AlbumScreen(navController : NavController){
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = true // Status bar 안보이기

    Scaffold(topBar = { // TODO 세팅 아이콘 -> 채팅 아이콘
        MainAppBar(title = "앨범", backIconEnable = false, icon = painterResource(id = CHATTING_ICON_BLACK), onIconClick = {
            navigateChatScreen(navController)
        })
    }) {
        OurHomeSurface() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                AlbumLazyVerticalGrid(photos = listOf("https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg"
                ,"https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg",
                    "https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg",
                    "https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg",
                    "https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg",
                    "https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg",
                    "https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg"), navController = navController)

                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }

}



/** 앨범 사진 Grid Lazy **/
@Composable
fun AlbumLazyVerticalGrid(photos: List<String>, navController: NavController){ // 사진은 id값, 날짜, 이미지 링크 필요

    val tmpList = listOf<List<String>>(photos, photos, photos, photos)

    LazyVerticalGrid(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp), columns = GridCells.Fixed(3)){
        tmpList.forEach {
            placeGridLine{
                AlbumDate(year = 2022, month = 10)
            }
            items(10){
                PhotoItem(photo = photos[0], onClick = {
                    navigateAlbumDetail(navController)
                })
            }

            placeGridLine {
                Spacer(modifier = Modifier.height(32.dp))
            }

        }

    }
}

/** 앨범 디테일 이동 **/
fun navigateAlbumDetail(navController: NavController) {//https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg
    navController.navigate(OurHomeScreens.AlbumDetailScreen.name + "/" +
            encodeUrlForNavigate("https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg") +
        "/" +
        "2022년 10월 22일"
    )
}

/** 사진 아이템 **/
@Composable
fun PhotoItem(photo: String, onClick : () -> Unit){ // 사진은 id값, 날짜, 이미지 링크 필요

    val painter =
        rememberAsyncImagePainter("https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg")

    Image(
        modifier = Modifier
            .size(120.dp)
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick.invoke()
            }
            ,
        painter = painter,
        contentScale = ContentScale.FillBounds,
        contentDescription = "펫 이미지",
    )
}


/** 앨범 날짜 **/
@Composable
fun AlbumDate(year: Int, month: Int){
    
    Text(modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), text = "${year}년 ${month}월",
        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold))
    
}



/** 그리드 한줄 차지 **/
fun LazyGridScope.placeGridLine(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}


fun encodeUrlForNavigate(url : String): String{
    return URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
}
package com.ssafy.ourhome.screens.album

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ssafy.domain.model.album.DomainAlbumDTO
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.question.navigateChatScreen
import com.ssafy.ourhome.startLoading
import com.ssafy.ourhome.stopLoading
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.utils.CHATTING_ICON_BLACK
import com.ssafy.ourhome.utils.State
import com.ssafy.ourhome.utils.optimizeBitmap
import java.io.File

@Composable
fun AlbumScreen(navController: NavController, vm: AlbumViewModel) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = true // Status bar 안보이기


    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        initAlbumScreen(vm)
    }
    initAlbumViewModelCallback(vm, context)

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                var file = Uri.fromFile(optimizeBitmap(context, uri)?.let { File(it) })

                vm.uploadAlbum(file)
            }
        }
    )

    // 이미지 업로드 성공
    when (vm.uploadProcessState) {
        State.SUCCESS -> {
            Toast.makeText(LocalContext.current, "사진 업로드에 성공했습니다.", Toast.LENGTH_SHORT).show()

            vm.setUploadProcessStateCompleted()
        }
        State.LOADING -> {
            startLoading()
        }
        State.COMPLETED -> {
            stopLoading()
            vm.setUploadProcessStateDefault()
        }
    }

    Scaffold(topBar = {
        MainAppBar(
            title = "앨범",
            backIconEnable = false,
            icon = painterResource(id = CHATTING_ICON_BLACK),
            onIconClick = {
                navigateChatScreen(navController)
            })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                imagePicker.launch("image/*")

            },
            backgroundColor = MainColor
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Floating Button")
        }
    }) {
        OurHomeSurface() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                AlbumLazyVerticalGrid(
                    vm = vm, navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }

}

fun initAlbumScreen(vm: AlbumViewModel) {
    vm.getAlbumImages()
}

fun initAlbumViewModelCallback(vm: AlbumViewModel, context: Context) {
    when (vm.getAlbumImagesProcessState.value) {
        State.ERROR -> {
            vm.getAlbumImagesProcessState.value = State.DEFAULT
            Toast.makeText(context, "앨범 사진을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
        State.SUCCESS -> {

        }
    }

}

/** 앨범 사진 Grid Lazy **/
@Composable
fun AlbumLazyVerticalGrid(
    vm: AlbumViewModel,
    navController: NavController
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp), columns = GridCells.Fixed(3)
    ) {
        vm.albumImages.forEach { (mapping_date, images) ->
            placeGridLine {
                AlbumDate(mapping_date)
            }
            items(images) {
                PhotoItem(image = it, onClick = {
                    vm.albumDetail = it
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
fun navigateAlbumDetail(navController: NavController) {
    navController.navigate(
        OurHomeScreens.AlbumDetailScreen.name
    )
}

/** 사진 아이템 **/
@Composable
fun PhotoItem(image: DomainAlbumDTO, onClick: () -> Unit) { // 사진은 id값, 날짜, 이미지 링크 필요

    val painter =
        rememberAsyncImagePainter(image.imageUri)

    Image(
        modifier = Modifier
            .size(120.dp)
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick.invoke()
            },
        painter = painter,
        contentScale = ContentScale.Crop,
        contentDescription = "펫 이미지",
    )
}


/** 앨범 날짜 **/
@Composable
fun AlbumDate(date: String) {

    Text(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
        text = date,
        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold)
    )

}


/** 그리드 한줄 차지 **/
fun LazyGridScope.placeGridLine(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

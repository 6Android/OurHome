package com.ssafy.ourhome.screens.album

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ssafy.ourhome.stopLoading
import com.ssafy.ourhome.ui.theme.RED
import com.ssafy.ourhome.utils.State

@Composable
fun AlbumDetailScreen(navController: NavController, vm: AlbumViewModel) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false // Status bar 안보이기
    val context = LocalContext.current


    var isVisible by remember {
        mutableStateOf(false)
    }

    val visibleDeleteDialogState = remember {
        mutableStateOf(false)
    }

    initAlbumDetailScreen(vm)
    initAlbumDetailViewModelCallback(vm, context, navController, visibleDeleteDialogState)

    /** 삭제하기 다이얼로그 */
    if (visibleDeleteDialogState.value) {
        AlertDialog(
            onConfirmClick = {
                vm.deleteAlbumImage()
            },
            onDismissRequest = { visibleDeleteDialogState.value = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                isVisible = isVisible.not()
            }
    ) {
        AlbumImage(rememberAsyncImagePainter(model = vm.albumDetail.imageUri))

        AlbumTopBar(
            isVisible,
            navController,
            "${vm.albumDetail.year}년 ${vm.albumDetail.month}월 ${vm.albumDetail.day}일",
            visibleDeleteDialogState,
            vm.visibleDeleteIconState
        )
    }
}

fun initAlbumDetailScreen(vm: AlbumViewModel) {
    vm.visibleDeleteIconState.value = false
    vm.initAlbumDetail()
}

fun initAlbumDetailViewModelCallback(
    vm: AlbumViewModel,
    context: Context,
    navController: NavController,
    visibleDeleteDialogState: MutableState<Boolean>
) {
    when (vm.deleteProcessState) {
        State.SUCCESS -> {
            Toast.makeText(context, "사진을 삭제했습니다.", Toast.LENGTH_SHORT).show()
            vm.setDeleteProcessStateCompleted()
            stopLoading()

            navController.popBackStack()
        }
        State.ERROR -> {
            Toast.makeText(context, "사진을 삭제하는데 실패했습니다", Toast.LENGTH_SHORT).show()
            vm.setDeleteProcessStateCompleted()
        }
        State.LOADING -> {
        }
        State.COMPLETED -> {
            visibleDeleteDialogState.value = false
            vm.setDeleteProcessStateDefault()
        }
    }
}

/** 엘범 탑 바 **/
@Composable
private fun AlbumTopBar(
    isVisible: Boolean,
    navController: NavController,
    photoDate: String,
    visibleDeleteDialogState: MutableState<Boolean>,
    visibleDeleteIconState: MutableState<Boolean>
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

            if (visibleDeleteIconState.value) {
                /** 휴지통 아이콘 **/
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.clickable { // 사진 삭제
                            visibleDeleteDialogState.value = true
                        },
                        tint = Color.White
                    )
                }
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

/** 경고 다이얼로그 창 */
@Composable
fun AlertDialog(
    onConfirmClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {

            AlertDialogContent("정말로 삭제하시겠습니까?", "삭제", onConfirmClick, onDismissRequest)
        }
    }
}

@Composable
fun AlertDialogContent(
    header: String,
    confirmText: String,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        /** 다이얼로그 헤더 */
        Text(text = header, style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlertRoundedButton(
                modifier = Modifier.weight(1F), label = confirmText
            ) {
                onConfirmClick()
            }
            Text(
                text = "닫기",
                style = MaterialTheme.typography.button.copy(color = Color.Gray),
                modifier = Modifier
                    .weight(1F)
                    .clickable { onDismissRequest() },
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun AlertRoundedButton(
    modifier: Modifier = Modifier,
    label: String = "Button",
    radius: Int = 20,
    onClick: () -> Unit = {}
) {

    Surface(
        modifier = modifier.clip(
            RoundedCornerShape(CornerSize(radius))
        ),
        color = RED
    ) {

        Column(
            modifier = modifier
                .heightIn(40.dp)
                .clickable { onClick.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.button, color = Color.White)
        }
    }
}
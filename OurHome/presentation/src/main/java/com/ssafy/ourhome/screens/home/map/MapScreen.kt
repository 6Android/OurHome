package com.ssafy.ourhome.screens.home.map

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.OurHomeSurface

@Composable
fun MapScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: MapViewModel
) {

    vm.getFamilyUsers()

    val width = remember {
        mutableStateOf(0)
    }
    val height = remember {
        mutableStateOf(0)
    }

    // 맵 처음 초기화 여부
    val initState = remember {
        mutableStateOf(false)
    }

    val visibleBottomSheetState = remember {
        mutableStateOf(false)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(72.715133, 126.734086), 10f)
    }

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(isMyLocationEnabled = true)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(mapToolbarEnabled = false, compassEnabled = false)
        )
    }

    OurHomeSurface() {

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    width.value = it.size.width
                    height.value = it.size.width
                },
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            properties = mapProperties
        ) {

            for (i in vm.users) {
                Marker(
                    state = MarkerState(position = LatLng(i.latitude, i.longitude)),
                    title = i.name,
                    snippet = positionUpdateFormatter(i.location_updated)
                )
            }

            val bounds = LatLngBounds.Builder()
            val list = arrayListOf<LatLng>()
            for (i in vm.users) {
                list.add(LatLng(i.latitude, i.longitude))
            }
            for (polyline in list) {
                bounds.include(polyline)
            }

            // 맨 처음 초기화 때만 모든 가족이 보이도록 위치를 갱신한다.
            if (vm.users.isNotEmpty()) {
                if (!initState.value) {
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds.build(),
                            width.value,
                            height.value,
                            (height.value * 0.05f).toInt()
                        )
                    )
                    initState.value = true
                }
            }

        }

        MapBackButton(navController)

        IconWithButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .clickable {
                    visibleBottomSheetState.value = true

                },
            icon = Icons.Default.List,
            title = "가족 목록"
        )

        /** 바텀 시트 */
        if (visibleBottomSheetState.value) {
            BottomSheet(vm.users, onDismissRequest = { visibleBottomSheetState.value = false }) {
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
                visibleBottomSheetState.value = false
            }
        }
    }
}

/** 바텀 시트 */
@Composable
fun BottomSheet(
    list: List<DomainUserDTO>,
    onDismissRequest: () -> Unit,
    onItemClick: (LatLng) -> Unit
) {
    BottomSheetDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        properties = BottomSheetDialogProperties()
    ) {
        // content
        Surface(
            modifier = Modifier,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                // 가족이 없을 때
                if (list.isEmpty()) {

                } else {
                    UserListView(list) {
                        onItemClick(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun UserListView(list: List<DomainUserDTO>, onItemClick: (LatLng) -> (Unit)) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = list) { user ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(LatLng(user.latitude, user.longitude))
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserImage(
                    modifier = Modifier.weight(2f),
                    imageUrl = user.image
                )
                Text(
                    text = user.name,
                    modifier = Modifier
                        .weight(5f)
                        .padding(horizontal = 32.dp),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )
                Column(
                    modifier = Modifier.weight(3f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "마지막 업데이트",
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = positionUpdateFormatter(user.location_updated),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

fun positionUpdateFormatter(
    lastUpdated: Long
): String {

    val curTime = System.currentTimeMillis()
    val returnTime = curTime - lastUpdated

    // 초, 분, 시, 일 (99일이 지나면 99일로 표기)
    return if (returnTime / (1000) < 60) {
        "${returnTime / (1000)} 초 전"
    } else if (returnTime / (1000 * 60) < 60) {
        "${returnTime / (1000 * 60)} 분 전"
    } else if (returnTime / (1000 * 60) < 1440) {
        "${(returnTime / (1000 * 60 * 60))} 시간 전"
    } else {
        if ((returnTime / (1000 * 60 * 60 * 24)) > 99) {
            "99 일 전"
        } else {
            "${(returnTime / (1000 * 60 * 60 * 24))} 일 전"
        }
    }
}

@Composable
private fun UserImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    Image(
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape),
        painter =
        if (imageUrl == "default") painterResource(R.drawable.img_default_user)
        else rememberAsyncImagePainter(imageUrl),
        contentDescription = "Profile Image"
    )
}

@Composable
private fun IconWithButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String
) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
        Icon(imageVector = icon, contentDescription = "Icon")
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun MapBackButton(navController: NavController) {
    IconButton(
        modifier = Modifier
            .padding(all = 12.dp)
            .clip(shape = RoundedCornerShape(2.dp))
            .background(color = Color.White.copy(alpha = 0.8f)),
        onClick = { navController.popBackStack() }) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "BackButton"
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun MapPreview() {
//    OurHomeTheme {
//        MapScreen()
//    }
//}
package com.ssafy.ourhome.screens.home.map

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.ui.theme.OurHomeTheme

@Composable
fun MapScreen(navController: NavController = NavController(LocalContext.current)) {

    // TODO : 임의
    val list = listOf(
        LatLng(37.715133, 126.734086),
        LatLng(36.715133, 128.734086),
        LatLng(35.715133, 139.734086),
        // 경도, 위도 1도가 약 110km.
    )

    var width = 0
    var height = 0

    val cameraPositionState = rememberCameraPositionState {

//        var startLat = 0.0
//        var startLng = 0.0
//        for(i in list){
//            startLat += i.latitude
//            startLng += i.longitude
//        }
//        startLat /= list.size
//        startLng /= list.size

//        position = CameraPosition.fromLatLngZoom(LatLng(37.715133,126.734086), 5f)

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
                    width = it.size.width
                    height = it.size.width
                },
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            properties = mapProperties
        ) {
            for (i in list) {
                Marker(
                    state = MarkerState(position = i),
                    title = "유저 123",
                    snippet = "Marker in Singapore"
                )
            }

            val bounds = LatLngBounds.Builder()
            for (polyline in list) {
                bounds.include(polyline)
            }

            cameraPositionState.move(
                CameraUpdateFactory.newLatLngBounds(
                    bounds.build(),
                    width,
                    height,
                    (height * 0.05f).toInt()
                )
            )
        }

        MapBackButton(navController)

        IconWithButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            icon = Icons.Default.List,
            title = "가족 목록"
        )
    }
}

@Composable
private fun IconWithButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String
) {
    Row(modifier = modifier
        .clip(shape = RoundedCornerShape(24.dp))
        .background(Color.White)
        .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(24.dp))
        .clickable {
            Log.d("test5", "IconWithButton: ")
        }
        .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
        Icon(imageVector = icon, contentDescription = "Icon")
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = title, style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold))
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

@Preview(showBackground = true)
@Composable
private fun MapPreview() {
    OurHomeTheme {
        MapScreen()
    }
}
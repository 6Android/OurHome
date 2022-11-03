package com.ssafy.ourhome.screens.userpage.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme


@Composable
fun ManageFamilyScreen(navController: NavController = NavController(LocalContext.current)) {

    //TODO : 나중에 DTO로 변경
    val tmp =
        listOf(
            listOf("default", "name"),
            listOf("imageUrl1", "name1"),
            listOf("imageUrl2", "name2")
        )

    Scaffold(topBar = {
        MainAppBar(title = "가족 관리", backIconEnable = true, onBackClick = {
            navController.popBackStack()
        })
    }) {
        OurHomeSurface() {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(items = tmp) { user ->
                    FamilyManageItem(user)

                }
            }
        }
    }


}

@Composable
fun FamilyManageItem(
    user: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            //이미지
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .padding(12.dp),
                painter =
                if (user[0] == "default") painterResource(R.drawable.img_default_user)
                else rememberAsyncImagePainter(user[0]),
                contentDescription = "Profile Image"
            )
            Column(
                modifier = Modifier
                    .height(100.dp)
                    .padding(4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = user[1],
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //버튼 2개

                    RoundedTextButton(
                        label = "가족장 위임", modifier = Modifier
                            .weight(1f)

                    ) {
                        //TODO : 가족장 위임 클릭이벤트
                    }

                    RoundedTextButton(
                        label = "가족 내보내기", modifier = Modifier
                            .weight(1f),
                        backGroundColor = Color(0xFFD9D9D9)
                    ) {
                        //TODO : 가족 내보내기 클릭이벤트
                    }

//                    Button(
//                        onClick = {},
//                        Modifier
//                            .weight(1f)
//                            .padding(end = 8.dp)
//                    ) {
//                        Text(text = "가족장 위임", style = MaterialTheme.typography.caption)
//                    }
//                    Button(
//                        onClick = {},
//                        Modifier
//                            .weight(1f)
//                            .padding(start = 8.dp),
//                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD9D9D9), contentColor = Color.Red)
//                    ) {
//                        Text(text = "가족 내보내기", style = MaterialTheme.typography.caption)
//
                }
            }

        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}


@Preview
@Composable
private fun ManagePreview() {
    OurHomeTheme {
        ManageFamilyScreen()
    }
}

@Composable
private fun RoundedTextButton(
    modifier: Modifier = Modifier,
    label: String = "Button",
    backGroundColor: Color = MainColor,
    radius: Int = 20,
    onClick: () -> Unit = {}
) {

    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(CornerSize(radius))
        ),
        color = backGroundColor
    ) {

        Column(
            modifier = modifier
                .width(110.dp)
                .height(40.dp)
                .clickable { onClick.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.button.copy(fontSize = 14.sp))
        }
    }
}

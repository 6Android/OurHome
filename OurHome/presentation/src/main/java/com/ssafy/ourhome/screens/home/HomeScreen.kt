package com.ssafy.ourhome.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter


data class Person(
    val imgUrl: String, val name: String
)

@Composable
fun HomeScreen(navController: NavController) {

    /** 더미 데이터 */
    val url =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvGknHPBUegusBb26NtSy4y47-yxr2Q_-Hwg&usqp=CAU"
    val person = Person(url, "아이유")
    val personList = arrayListOf(person, person, person, person)
    /** 더미 데이터 */

    Scaffold(topBar = {

        /** 상단 바 */
        TopAppBar(
            modifier = Modifier
                .height(200.dp)
                .shadow(
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                    elevation = 4.dp
                ),
        ) {
            Column() {

                /** 홈 툴바 */
                HomeToolBar()

                Spacer(modifier = Modifier.height(32.dp))

                /** 구성원 리스트 */
                PersonList(personList)
                Spacer(modifier = Modifier.height(50.dp))

                // todo: 가족위치
                // todo: 초대하기
                // todo: 달력
            }

        }

    }) {

    }
}

/** 홈 툴바 */
@Composable
private fun HomeToolBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        /** 로고 */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Home,
                contentDescription = "icon home"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.offset(y = (-2).dp),
                text = "우리집",
                style = MaterialTheme.typography.h5
            )
        }

        /** 채팅 아이콘 */
        Icon(imageVector = Icons.Default.Chat, contentDescription = "icon chat")
    }
}

/** 구성원 리스트 */
@Composable
private fun PersonList(personList: ArrayList<Person>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = personList, itemContent = { item ->
                /** 구성원 리스트 아이템 */
                PersonListItem(item)
            })
        }
    }
}

/** 구성원 리스트 아이템 */
@Composable
private fun PersonListItem(item: Person) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(item.imgUrl),
            contentDescription = "Profile Image"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.name,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
        )
    }
}
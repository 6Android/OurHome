package com.ssafy.ourhome.screens.home.schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.utils.Person
import com.ssafy.ourhome.utils.personList


@Composable
fun AddMemberScreen(navController: NavController) {
    val familyState = remember {
        mutableStateOf(personList)
    }
    Scaffold(
        topBar = {
            MainAppBar(
                title = "함께하는 가족들",
                backIconEnable = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) {
        OurHomeSurface {
            /** 가족 리스트  */
            FamilyList(familyState.value) {

            }

            /** 확인 버튼 */
            ConfirmButton {
                navController.popBackStack()
            }
        }
    }
}

/** 가족 리스트  */
@Composable
fun FamilyList(list: List<Person>, onItemClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(0.9f),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { }
        items(list) { person ->
            FamilyListItem(person = person, onItemClick = onItemClick)
        }
        item { }
    }
}

/** 가족 리스트 아이템 */
@Composable
fun FamilyListItem(person: Person, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(person.imgUrl),
                contentDescription = "Profile Image"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = person.name, style = MaterialTheme.typography.body2)
        }
        if (person.checked) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_checked),
                contentDescription = "checked"
            )
        } else {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_unchecked),
                contentDescription = "checked"
            )
        }
    }
}

/** 확인 버튼 */
@Composable
fun ConfirmButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), contentAlignment = Alignment.BottomCenter
    ) {
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), label = "확인",
            onClick = onClick
        )
    }
}
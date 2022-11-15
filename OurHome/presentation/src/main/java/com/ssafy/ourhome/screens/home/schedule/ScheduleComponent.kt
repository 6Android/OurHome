package com.ssafy.ourhome.screens.home.schedule

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.TextInput
import java.time.LocalDate


/** 날짜 */
@Composable
fun SelectDate(
    dateState: MutableState<LocalDate>,
    onDateClick: () -> Unit = {}
) {
    Text(
        modifier = Modifier.offset(x = 8.dp),
        text = "날짜",
        style = MaterialTheme.typography.subtitle2
    )
    Button(
        border = BorderStroke(width = 2.dp, color = Color.LightGray),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        elevation = ButtonDefaults.elevation(0.dp),
        onClick = { onDateClick() }) {
        Text(
            text = "${dateState.value}",
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
        )
    }
}


/** 제목 */
@Composable
fun WriteTitle(titleState: MutableState<String>, isEditable: Boolean = true) {
    Text(
        modifier = Modifier.offset(x = 8.dp),
        text = "제목",
        style = MaterialTheme.typography.subtitle2
    )
    TextInput(
        valueState = titleState,
        placeholder = "제목을 입력해주세요",
        enabled = isEditable
    )
}

/** 내용 */
@Composable
fun WriteContent(contentState: MutableState<String>, isEditable: Boolean = true) {
    Text(
        modifier = Modifier.offset(x = 8.dp),
        text = "내용",
        style = MaterialTheme.typography.subtitle2
    )
    TextInput(
        valueState = contentState,
        placeholder = "간단한 내용을 작성해주세요.",
        enabled = isEditable
    )
}

/** 함께하는 가족들 리스트 */
@Composable
fun PersonList(personList: List<DomainUserDTO>, isEditable: Boolean = true, onAddClick: () -> Unit, onDeleteClick: (String) -> Unit = {}) {
    Row {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.width(8.dp))
            }
            items(items = personList, itemContent = { item ->

                /** 구성원 리스트 아이템 */
                PersonListItem(item, isEditable, onDeleteClick)
            })
            if (isEditable) {
                item {

                    /** 가족 추가 버튼 */
                    Icon(
                        modifier = Modifier
                            .size(64.dp)
                            .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
                            .clip(CircleShape)
                            .clickable {
                                // todo: 가족 구성원 추가 화면으로 이동
                                onAddClick()
                            }
                            .padding(12.dp),
                        painter = rememberVectorPainter(Icons.Default.Add),
                        tint = Color.Gray,
                        contentDescription = "Profile Image"
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

/** 함께하는 가족들 리스트 아이템 */
@Composable
fun PersonListItem(item: DomainUserDTO, isEditable: Boolean = true, onDeleteClick: (String) -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box() {

            /** 프사 */
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                painter = if (item.image == "default") painterResource(R.drawable.img_default_user)
                else rememberAsyncImagePainter(item.image),
                contentDescription = "Profile Image"
            )

            /** 가족 구성원 삭제 버튼 */
            if (isEditable) {
                Image(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                        .clickable {
                            // todo: 구성원 삭제
                            onDeleteClick(item.email)
                        }
                        .background(Color.White)
                        .padding(2.dp)
                        .align(Alignment.TopEnd),
                    contentScale = ContentScale.Crop,
                    painter = rememberVectorPainter(Icons.Default.Close),
                    contentDescription = "Profile Image"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        /** 이름 */
        Text(
            text = item.name,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
    }
}
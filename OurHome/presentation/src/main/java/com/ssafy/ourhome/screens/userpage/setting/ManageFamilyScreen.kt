package com.ssafy.ourhome.screens.userpage.setting

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.utils.Prefs


@Composable
fun ManageFamilyScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: SettingViewModel
) {

    // 가족원 전부 불러오기
    vm.getFamilyUsers()

    // 가족장 위임
    if (vm.editSuccess) {
        navController.popBackStack()
        navController.popBackStack()
        vm.setEditSuccess()
        Toast.makeText(LocalContext.current, "가족장이 위임되었습니다.", Toast.LENGTH_SHORT).show()
    }

    // 가족 내보내기
    if (vm.transferSuccess) {
        vm.setTransferSuccess()
        Toast.makeText(LocalContext.current, "가족원을 내보냈습니다.", Toast.LENGTH_SHORT).show()
    }

    Scaffold(topBar = {
        MainAppBar(title = "가족 관리", backIconEnable = true, onBackClick = {
            navController.popBackStack()
        })
    }) {
        OurHomeSurface() {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                items(items = vm.users) { user ->
                    if (Prefs.email != user.email) {
                        FamilyManageItem(user, editManager = {
                            vm.editManager(it)
                        },
                            transferUser = {
                                vm.transferUserData(it)
                            })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }


}

@Composable
fun FamilyManageItem(
    user: DomainUserDTO,
    editManager: (String) -> Unit,
    transferUser: (DomainUserDTO) -> Unit
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
                contentScale = ContentScale.Crop,
                painter =
                if (user.image == "default") painterResource(R.drawable.img_default_user)
                else rememberAsyncImagePainter(user.image),
                contentDescription = "Profile Image"
            )
            Column(
                modifier = Modifier
                    .height(100.dp)
                    .padding(4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RoundedTextButton(
                        label = "가족장 위임", modifier = Modifier
                            .weight(1f)
                    ) {
                        editManager.invoke(user.email)
                    }

                    RoundedTextButton(
                        label = "가족 내보내기", modifier = Modifier
                            .weight(1f),
                        backGroundColor = Color(0xFFD9D9D9)
                    ) {
                        transferUser.invoke(user)
                    }

                }
            }

        }
    }
}


//@Preview
//@Composable
//private fun ManagePreview() {
//    OurHomeTheme {
//        ManageFamilyScreen()
//    }
//}

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

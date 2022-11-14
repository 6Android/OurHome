package com.ssafy.ourhome.screens.userpage.setting

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.ourhome.MainActivity
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.userpage.UserPageViewModel
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.checkAndRequestLocationPermissions
import com.ssafy.ourhome.utils.permissions

@Composable
fun SettingScreen(navController: NavController, vm: UserPageViewModel) {

    val switchChecked = remember {
        mutableStateOf(vm.user.location_permit)
    }
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    Scaffold(topBar = {
        MainAppBar(title = "설정", backIconEnable = true, onBackClick = {
            navController.popBackStack()
        })
    }) {
        // TODO : 옵션 로직 작성
        OurHomeSurface() {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                TextWithSwitch(title = "위치 공유 허용", isChecked = switchChecked, context) {

                    if (it) {
                        vm.editLocationPermission(true)
                        MainActivity.startWorkManager()
                    } else {
                        vm.editLocationPermission(false)
                        MainActivity.stopWorkManager()
                    }

                }

                Spacer(modifier = Modifier.height(42.dp))
                OurHomeSetting(code = Prefs.familyCode, navController, vm.user.manager, context)

                Spacer(modifier = Modifier.height(42.dp))
                Support()

                Spacer(modifier = Modifier.height(56.dp))
                ClickableText("로그아웃") {
                    vm.logout()
                    val intent = Intent(context,MainActivity::class.java)
                    context.startActivity(intent)
                    (context as MainActivity).finish()
                }
                Spacer(modifier = Modifier.height(32.dp))

                // TEST : 유저 넣기
                ClickableText("회원탈퇴") {
                    vm.insertUser(DomainUserDTO(email= "test1@naver.com", name = "테스트1", family_code = "TEST"))
                    vm.insertUser(DomainUserDTO(email= "test2@naver.com", name = "테스트2",family_code = "TEST"))
                    vm.insertUser(DomainUserDTO(email= "test3@naver.com", name = "테스트3",family_code = "TEST"))
                    vm.insertUser(DomainUserDTO(email= "test4@naver.com", name = "테스트4",family_code = "TEST"))
                    vm.insertUser(DomainUserDTO(email= "test5@naver.com", name = "테스트5",family_code = "TEST"))
                }
            }
        }
    }
}

@Composable
private fun OurHomeSetting(
    code: String,
    navController: NavController,
    isManager: Boolean,
    context: Context
) {
    TextHeader(title = "가족 설정")
    Spacer(modifier = Modifier.height(26.dp))

    // TODO : 클릭 이벤트
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        TextWithCode(title = "우리집 코드", code = code) {

        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "가족 관리") {
            if(isManager){
                navController.navigate(OurHomeScreens.ManageFamilyScreen.name)
            }else{
                Toast.makeText(context, "가족장 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "가족 끊기") {

        }
    }
}

@Composable
private fun ClickableText(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        modifier = Modifier.clickable {
            onClick.invoke()
        },
        style = MaterialTheme.typography.body1.copy(
            fontSize = 18.sp, fontWeight = FontWeight.ExtraBold
        )
    )
}

@Composable
private fun Support() {
    TextHeader(title = "고객 지원")
    Spacer(modifier = Modifier.height(26.dp))

    // TODO : 클릭 이벤트
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        TextWithNext(title = "이용 약관") {

        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "문의 보내기") {

        }
    }
}

@Composable
private fun TextWithSwitch(
    title: String,
    isChecked: MutableState<Boolean>,
    context: Context,
    onClick: (Boolean) -> Unit
) {

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        /** 권한 요청시 동의 했을 경우 **/
        if (areGranted) {
            onClick(true)
            isChecked.value = true
        }
        /** 권한 요청시 거부 했을 경우 **/
        else {
            Toast.makeText(context, "가족의 위치를 확인하기 위해 위치 권한을 반드시 동의해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, style = MaterialTheme.typography.body1.copy(
                fontSize = 18.sp, fontWeight = FontWeight.ExtraBold
            )
        )

        Switch(
            checked = isChecked.value,
            onCheckedChange = {
                if (it) {
                    /** 위치 권한 체크 **/
                    checkAndRequestLocationPermissions(
                        context, permissions, launcherMultiplePermissions
                    ) {
                        /** 이미 권한 있을 때 **/
                        onClick(true)
                        isChecked.value = it
                    }
                } else {
                    onClick(false)
                    isChecked.value = it
                }


            },
            modifier = Modifier
                .padding(all = 0.dp)
                .width(48.dp),
            colors = SwitchDefaults.colors(
                checkedTrackColor = MainColor,
                checkedTrackAlpha = 1.0f,
                uncheckedTrackColor = Color.Gray,
                checkedThumbColor = Color.White
            )
        )
    }
}

@Composable
private fun TextWithNext(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, style = MaterialTheme.typography.body1.copy(
                fontSize = 18.sp, fontWeight = FontWeight.Normal
            )
        )
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = "Next Button")
    }
}

@Composable
private fun TextHeader(
    title: String
) {
    Text(
        text = title, style = MaterialTheme.typography.body1.copy(
            fontSize = 18.sp, fontWeight = FontWeight.ExtraBold
        )
    )
}

@Composable
private fun TextWithCode(
    title: String,
    code: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, style = MaterialTheme.typography.body1.copy(
                fontSize = 18.sp, fontWeight = FontWeight.Normal
            )
        )
        Text(
            text = code, style = MaterialTheme.typography.body1.copy(
                fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF707070)
            )
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun SettingPreview() {
//    OurHomeTheme {
//        SettingScreen()
//    }
//}

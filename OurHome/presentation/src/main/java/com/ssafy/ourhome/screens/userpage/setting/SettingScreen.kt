package com.ssafy.ourhome.screens.userpage.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.ourhome.MainActivity
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeAlertDialog
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.userpage.UserPageViewModel
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.checkAndRequestLocationPermissions
import com.ssafy.ourhome.utils.permissions

@Composable
fun SettingScreen(navController: NavController, vm: UserPageViewModel) {

    LaunchedEffect(key1 = true) {
        // 가족원 전부 불러오기
        vm.getFamilyUsers()
    }

    val switchChecked = remember {
        mutableStateOf(vm.user.location_permit)
    }
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val visibleTransferDialogState = remember {
        mutableStateOf(false)
    }

    val isManager = remember {
        mutableStateOf(vm.user.manager)
    }

    //가족 끊기 Dialog
    if (visibleTransferDialogState.value) {
        OurHomeAlertDialog(
            header = "가족을 나가시겠습니까?",
            confirmText = "나가기",
            onConfirmClick = {
                visibleTransferDialogState.value = false
                // 가족장이 아닌 경우
                if(!isManager.value) {
                    vm.job.cancel()
                    vm.transferUserData(vm.user)
                    return@OurHomeAlertDialog
                }

                if(vm.users.size == 1) {
                    vm.job.cancel()
                    vm.transferUserData(vm.user)
                    return@OurHomeAlertDialog
                }

                Toast.makeText(context, "가족장은 탈퇴할 수 없습니다.", Toast.LENGTH_SHORT).show()
            },
            onDismissRequest = { visibleTransferDialogState.value = false }
        )
    }

    if (vm.transferSuccess) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        (context as MainActivity).finish()
        vm.setTransferSuccess()
    }


    Scaffold(topBar = {
        MainAppBar(title = "설정", backIconEnable = true, onBackClick = {
            navController.popBackStack()
        })
    }) {
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
                OurHomeSetting(
                    code = Prefs.familyCode,
                    navAction = { navController.navigate(OurHomeScreens.ManageFamilyScreen.name) },
                    visibleTransferDialogState,
                    vm.user.manager,
                    context
                )

                Spacer(modifier = Modifier.height(42.dp))
                Support(context)

                Spacer(modifier = Modifier.height(56.dp))
                ClickableText("로그아웃") {
                    vm.logout()
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as MainActivity).finish()
                }
                Spacer(modifier = Modifier.height(32.dp))

            }
        }
    }
}

@Composable
private fun OurHomeSetting(
    code: String,
    navAction: () -> Unit,
    visibleTransferDialogState: MutableState<Boolean>,
    isManager: Boolean,
    context: Context
) {
    TextHeader(title = "가족 설정")
    Spacer(modifier = Modifier.height(26.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        TextWithCode(title = "우리집 코드", code = code) {
            val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("FamilyCode", code)
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(context, "우리집 코드가 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "가족 관리") {
            if (isManager) {
                navAction()

            } else {
                Toast.makeText(context, "가족장 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "가족 끊기") {
            visibleTransferDialogState.value = true
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
private fun Support(
    context: Context
) {
    TextHeader(title = "고객 지원")
    Spacer(modifier = Modifier.height(26.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        TextWithNext(title = "이용 약관") {
            val i = Intent(Intent.ACTION_VIEW)
            i.data =
                Uri.parse("https://sweltering-enthusiasm-d6a.notion.site/02cbd0ae63ac4030a30887a6d1ee3138")
            context.startActivity(i)
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "문의 보내기") {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("http://pf.kakao.com/_xktsxfxj")
            context.startActivity(i)
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

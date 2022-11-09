package com.ssafy.ourhome.screens.userpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.utils.SETTING_ICON

@Composable
fun UserPageScreen(
    navController: NavController,
    email: String
) {
    val scrollState = rememberScrollState()

    val vm: UserPageViewModel = hiltViewModel()

    // TODO : 패밀리코드, 유저 이메일
    vm.getProfile("EX7342", email = email)

    val user = remember {
        mutableStateOf(DomainUserDTO())
    }

    when (val userResponse = vm.userResponse) {
        is ResultType.Uninitialized -> {}
        is ResultType.Success -> {
            user.value = userResponse.data
        }
        is ResultType.Error -> print(userResponse.exception)
    }

    Scaffold(topBar = {
        MainAppBar(
            title = "유저 정보",
            backIconEnable = true,
            onBackClick = {
                navController.popBackStack()
            }
        )
    }) {

        OurHomeSurface {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.padding(top = 16.dp))

                UserInfoCard(userDTO = user.value, navController)

                Spacer(modifier = Modifier.height(16.dp))
                UserColorCardList(userDTO = user.value)

                Spacer(modifier = Modifier.height(16.dp))
                UserCommonCardList(userDTO = user.value)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun UserPagePreview() {
//    OurHomeTheme {
//        UserPageScreen()
//    }
//}
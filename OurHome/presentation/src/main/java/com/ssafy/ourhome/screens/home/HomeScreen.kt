package com.ssafy.ourhome.screens.home

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.kizitonwose.calendar.core.CalendarDay
import com.ssafy.domain.model.schedule.DomainScheduleDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.ourhome.MainActivity.Companion.startWorkManager
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.startLoading
import com.ssafy.ourhome.stopLoading
import com.ssafy.ourhome.screens.question.navigateChatScreen
import com.ssafy.ourhome.utils.*

/** 맵 화면 이동 **/
fun moveMap(navController: NavController, vm: HomeViewModel) {
    // 맵 화면이동
    navController.navigate(OurHomeScreens.MapScreen.name)

    // 스위치 on
    vm.editLocationPermission(true)

    // 워크매니저 시작
    startWorkManager()
}

@Composable
fun HomeScreen(navController: NavController, vm: HomeViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val visibleBottomSheetState = remember {
        mutableStateOf(false)
    }
    val visibleMoveToQuestionDialogState = remember {
        mutableStateOf(false)
    }
    val onScheduleClick: (DomainScheduleDTO) -> Unit = { schedule ->
        vm.setScheduleDetail(schedule)
        visibleBottomSheetState.value = false
        navController.navigate(OurHomeScreens.ScheduleDetailScreen.name)
    }
    val visibleInviteDialogState = remember {
        mutableStateOf(false)
    }


    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        /** 권한 요청시 동의 했을 경우 **/
        if (areGranted) {
            moveMap(navController, vm)
        }
        /** 권한 요청시 거부 했을 경우 **/
        else {
            Toast.makeText(context, "가족의 위치를 확인하기 위해 위치 권한을 반드시 동의해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    /** 달력에 필요한 데이터 */
    var selection = remember { mutableStateOf<CalendarDay?>(null) }
    val onMonthChangeListener: (String) -> Unit = {
        it.split("-").let {
            val year = it[0].toInt()
            val month = it[1].toInt()

            // vm 호출
            vm.getFamilySchedules(year, month)
        }

    }
    LaunchedEffect(key1 = "") {
        vm.getFamilyUsers()

        if (vm.checkAnswerTodayQuestionProcessState.value != State.COMPLETED) vm.checkAnswerTodayQuestion()

    }

    when (vm.familyUsersProcessState.value) {
        State.SUCCESS -> {
            // 스케줄 추가 상태 초기화
            vm.initAddSchedule()
            vm.familyUsersProcessState.value = State.DEFAULT
        }
        State.ERROR -> {
            Toast.makeText(context, "가족 정보를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
            vm.familyUsersProcessState.value = State.DEFAULT
        }
    }

    when (vm.checkAnswerTodayQuestionProcessState.value) {
        // 오늘의 질문에 이미 대답한 경우
        State.SUCCESS -> {
            vm.checkAnswerTodayQuestionProcessState.value = State.COMPLETED
        }
        // 오늘의 질문에 대답 안한 경우
        State.FAIL -> {
            visibleMoveToQuestionDialogState.value = true
            vm.checkAnswerTodayQuestionProcessState.value = State.COMPLETED
        }
        // 에러
        State.ERROR -> {
            vm.checkAnswerTodayQuestionProcessState.value = State.COMPLETED
        }
        State.LOADING -> {
            startLoading()
        }
        State.COMPLETED -> {
            stopLoading()
        }
    }

    Scaffold(topBar = {

        /** 상단 바 */
        TopAppBar(
            modifier = Modifier
                .height(170.dp)
                .shadow(
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    elevation = 4.dp
                ),
        ) {
            Column() {

                /** 홈 툴바 */
                HomeToolBar {
                    navigateChatScreen(navController)
                }

                Spacer(modifier = Modifier.height(16.dp))

                /** 구성원 리스트 */
                PersonList(vm.familyUsersState.value) {

                    // 내 이미지 클릭했을 경우 바텀 네비 마이페이지로 이동
                    if (it == Prefs.email) {
                        navController.navigate(BottomNavItem.MyPage.screenRoute)
                    } else {
                        navController.navigate(OurHomeScreens.UserPageScreen.name + "/$it")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }) {
        OurHomeSurface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    /** 가족위치 카드 */
                    HomeCard(
                        modifier = Modifier.weight(1f),
                        title = "가족위치",
                        content = "지도에서 가족위치를 확인하세요!",
                        image = painterResource(
                            id = R.drawable.ic_map
                        )
                    ) {
                        checkAndRequestLocationPermissions(
                            context,
                            permissions,
                            launcherMultiplePermissions
                        ) {
                            moveMap(navController, vm)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    /** 초대하기 카드 */
                    HomeCard(
                        modifier = Modifier.weight(1f),
                        title = "초대하기",
                        content = "우리집에 가족을 초대해보세요!",
                        image = painterResource(
                            id = R.drawable.ic_invite
                        )
                    ) {
                        visibleInviteDialogState.value = true
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                /** 달력 카드 */
                CalendarCard(
                    visibleBottomSheetState = visibleBottomSheetState,
                    selection = selection,
                    map = vm.scheduleMap,
                    onMonthChange = onMonthChangeListener
                )

                Spacer(modifier = Modifier.height(16.dp))

                /** 바텀 시트 */
                if (visibleBottomSheetState.value) {
                    BottomSheet(
                        list = vm.scheduleMap.getOrDefault(
                            "${selection.value!!.date.year}-${selection.value!!.date.monthValue.toFillZeroString()}-${selection.value!!.date.dayOfMonth.toFillZeroString()}",
                            emptyList()
                        ),
                        onAddScheduleClick = {
                            vm.addScheduleDateState.value = selection.value!!.date
                            visibleBottomSheetState.value = false
                            moveToAddScheduleScreen(navController)
                        },
                        onScheduleClick = onScheduleClick
                    ) {
                        visibleBottomSheetState.value = false
                    }
                }

                /** 오늘의 질문으로 가기 다이얼로그 */
                if (visibleMoveToQuestionDialogState.value) {
                    TodayQuestionDialog(
                        onPositiveClick = { navController.navigate(BottomNavItem.Question.screenRoute) },
                        onDismissRequest = { visibleMoveToQuestionDialogState.value = false }
                    )
                }

                /** 초대하기 다이얼로그 */
                if (visibleInviteDialogState.value) {
                    InviteDialog(
                        familyCode = Prefs.familyCode,
                        onShareClick = {
                            shareFamilyCode(context, Prefs.familyCode)
                        },
                        onDismissRequest = { visibleInviteDialogState.value = false }
                    )
                }
            }
        }
    }
}

/** 오늘의 질문으로 가기 다이얼로그 */
@Composable
private fun TodayQuestionDialog(
    onPositiveClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            /** 오늘의 질문으로 가기 다이얼로그 내용 */
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                /** 다이얼로그 헤더 */
                Text(text = "오늘의 질문 알림", style = MaterialTheme.typography.subtitle1)

                Spacer(modifier = Modifier.height(32.dp))

                /** 아이콘 */
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(60.dp),
                        painter = painterResource(
                            id = R.drawable.img_question
                        ),
                        contentDescription = "icon"
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                /** 우리집 코드 */
                Text(
                    text = "아직 오늘의 질문에 답하지 않았습니다",
                    style = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.height(32.dp))

                /** 답변하러가기 버튼 */
                RoundedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), label = "답변하러 가기"
                ) {
                    onPositiveClick()
                }
            }
        }
    }
}

/** 홈 툴바 */
@Composable
private fun HomeToolBar(
    onChatClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        /** 로고 */
        Row(verticalAlignment = Alignment.CenterVertically) {

            /** 로고 아이콘 */
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Home,
                tint = Color.White,
                contentDescription = "icon home"
            )
            Spacer(modifier = Modifier.width(8.dp))

            /** 앱 이름 */
            Text(
                modifier = Modifier.offset(y = (-2).dp),
                text = "우리집",
                style = MaterialTheme.typography.subtitle1.copy(color = Color.White)
            )
        }

        /** 채팅 아이콘 */
        Icon(
            modifier = Modifier
                .size(32.dp)
                .clickable { onChatClick() },
            painter = painterResource(id = R.drawable.ic_chat_white),
            contentDescription = "icon chat",
            tint = Color.White
        )
    }
}

/** 구성원 리스트 */
@Composable
private fun PersonList(personList: List<DomainUserDTO>, onImageClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { }
            items(items = personList, itemContent = { item ->
                /** 구성원 리스트 아이템 */
                PersonListItem(item, onImageClick)
            })
            item { }
        }
    }
}

/** 구성원 리스트 아이템 */
@Composable
private fun PersonListItem(item: DomainUserDTO, onImageClick: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        /** 프사 */
        Box(
            modifier = Modifier.size(64.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .clickable { onImageClick(item.email) },
                contentScale = ContentScale.Crop,
                painter =
                if (item.image == "default") painterResource(R.drawable.img_default_user)
                else rememberAsyncImagePainter(item.image),
                contentDescription = "Profile Image"
            )

            if (item.manager) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .align(Alignment.BottomEnd),
                    painter = painterResource(R.drawable.img_manager),
                    contentDescription = "Manager Image",
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        /** 이름 */
        Text(
            text = item.name,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}


/** 상단 카드 */
@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    image: Painter,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            /** 헤더 */
            Text(text = title, style = MaterialTheme.typography.subtitle2)
            Spacer(modifier = Modifier.height(12.dp))

            /** 내용 */
            Text(
                text = content,
                style = MaterialTheme.typography.body2.copy(
                    lineHeight = 20.sp,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            /** 아이콘 */
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = image,
                    contentDescription = "logo"
                )
            }
        }
    }
}

/** 바텀 시트 */
@Composable
fun BottomSheet(
    list: List<DomainScheduleDTO>,
    onAddScheduleClick: () -> Unit,
    onScheduleClick: (DomainScheduleDTO) -> Unit,
    onDismissRequest: () -> Unit
) {
    BottomSheetDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        properties = BottomSheetDialogProperties(

        )
    ) {
        // content
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /** 바텀 시트 스크롤 바 */
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(3.dp)
                        .background(Color.LightGray),
                ) {

                }

                Spacer(modifier = Modifier.height(16.dp))

                /** 일정이 없을 경우 */
                if (list.isEmpty()) {
                    NoScheduleItem(onAddScheduleClick = onAddScheduleClick)
                }
                /** 있을 경우 */
                else {
                    ScheduleList(
                        list = list,
                        onAddScheduleClick = onAddScheduleClick,
                        onScheduleClick = onScheduleClick
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/** 일정이 있을 경우 바텀시트 */
@Composable
private fun ScheduleList(
    list: List<DomainScheduleDTO>,
    onAddScheduleClick: () -> Unit,
    onScheduleClick: (DomainScheduleDTO) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        /** 바텀 시트 헤더 */
        Text(text = "오늘의 일정", style = MaterialTheme.typography.subtitle2)

        /** 바텀 시트 일정 추가 */
        Text(
            modifier = Modifier.clickable {
                // 일정 추가 화면으로 이동
                onAddScheduleClick()
            },
            text = "일정 추가",
            style = MaterialTheme.typography.body2.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    /** 바텀 시트 일정 리스트 */
    TodayScheduleList(list = list, onScheduleClick = onScheduleClick)
}

/** 일정이 없을 경우 바텀시트 */
@Composable
private fun NoScheduleItem(onAddScheduleClick: () -> Unit) {
    /** 헤더 */
    Text(
        text = "등록된 일정이 없습니다",
        style = MaterialTheme.typography.subtitle2
    )

    Spacer(modifier = Modifier.height(16.dp))

    /** 달력 이미지 */
    Image(
        modifier = Modifier
            .size(112.dp)
            .clickable {
                // 일정 추가 화면으로 이동
                onAddScheduleClick()
            },
        painter = painterResource(id = R.drawable.ic_calendar_add),
        contentDescription = "calendar add"
    )

    Spacer(modifier = Modifier.height(16.dp))

    /** 내용 */
    Text(
        text = "가족과 함께하는 일정을 추가해보세요!",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
    )
}

/** 일정 추가 화면으로 가는 함수 */
fun moveToAddScheduleScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.AddScheduleScreen.name)
}

/** 초대하기 다이얼로그 창 */
@Composable
fun InviteDialog(
    familyCode: String,
    onShareClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            /** 참여코드 다이얼로그 내용 */
            InviteDialogContent(familyCode, onShareClick, onDismissRequest)
        }
    }
}

/** 초대하기 다이얼로그 내용 */
@Composable
fun InviteDialogContent(
    familyCode: String,
    onShareClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        /** 다이얼로그 헤더 */
        Text(text = "우리집 코드", style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.height(32.dp))

        /** 우리집 코드 */
        Text(
            text = familyCode,
            style = MaterialTheme.typography.h2.copy(
                color = Color.Gray,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(60.dp))

        /** 공유하기 버튼 */
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), label = "카카오톡으로 공유"
        ) {
            onShareClick()
        }
        Text(
            text = "닫기",
            style = MaterialTheme.typography.button.copy(color = Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDismissRequest() }
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

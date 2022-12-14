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

/** ??? ?????? ?????? **/
fun moveMap(navController: NavController, vm: HomeViewModel) {
    // ??? ????????????
    navController.navigate(OurHomeScreens.MapScreen.name)

    // ????????? on
    vm.editLocationPermission(true)

    // ??????????????? ??????
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
        /** ?????? ????????? ?????? ?????? ?????? **/
        if (areGranted) {
            moveMap(navController, vm)
        }
        /** ?????? ????????? ?????? ?????? ?????? **/
        else {
            Toast.makeText(context, "????????? ????????? ???????????? ?????? ?????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
        }
    }

    /** ????????? ????????? ????????? */
    var selection = remember { mutableStateOf<CalendarDay?>(null) }
    val onMonthChangeListener: (String) -> Unit = {
        it.split("-").let {
            val year = it[0].toInt()
            val month = it[1].toInt()

            // vm ??????
            vm.getFamilySchedules(year, month)
        }

    }
    LaunchedEffect(key1 = "") {
        vm.getFamilyUsers()

        if (vm.checkAnswerTodayQuestionProcessState.value != State.COMPLETED) vm.checkAnswerTodayQuestion()

    }

    when (vm.familyUsersProcessState.value) {
        State.SUCCESS -> {
            // ????????? ?????? ?????? ?????????
            vm.initAddSchedule()
            vm.familyUsersProcessState.value = State.DEFAULT
        }
        State.ERROR -> {
            Toast.makeText(context, "?????? ????????? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show()
            vm.familyUsersProcessState.value = State.DEFAULT
        }
    }

    when (vm.checkAnswerTodayQuestionProcessState.value) {
        // ????????? ????????? ?????? ????????? ??????
        State.SUCCESS -> {
            vm.checkAnswerTodayQuestionProcessState.value = State.COMPLETED
        }
        // ????????? ????????? ?????? ?????? ??????
        State.FAIL -> {
            visibleMoveToQuestionDialogState.value = true
            vm.checkAnswerTodayQuestionProcessState.value = State.COMPLETED
        }
        // ??????
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

        /** ?????? ??? */
        TopAppBar(
            modifier = Modifier
                .height(170.dp)
                .shadow(
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    elevation = 4.dp
                ),
        ) {
            Column() {

                /** ??? ?????? */
                HomeToolBar {
                    navigateChatScreen(navController)
                }

                Spacer(modifier = Modifier.height(16.dp))

                /** ????????? ????????? */
                PersonList(vm.familyUsersState.value) {

                    // ??? ????????? ???????????? ?????? ?????? ?????? ?????????????????? ??????
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

                    /** ???????????? ?????? */
                    HomeCard(
                        modifier = Modifier.weight(1f),
                        title = "????????????",
                        content = "???????????? ??????????????? ???????????????!",
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

                    /** ???????????? ?????? */
                    HomeCard(
                        modifier = Modifier.weight(1f),
                        title = "????????????",
                        content = "???????????? ????????? ??????????????????!",
                        image = painterResource(
                            id = R.drawable.ic_invite
                        )
                    ) {
                        visibleInviteDialogState.value = true
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                /** ?????? ?????? */
                CalendarCard(
                    visibleBottomSheetState = visibleBottomSheetState,
                    selection = selection,
                    map = vm.scheduleMap,
                    onMonthChange = onMonthChangeListener
                )

                Spacer(modifier = Modifier.height(16.dp))

                /** ?????? ?????? */
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

                /** ????????? ???????????? ?????? ??????????????? */
                if (visibleMoveToQuestionDialogState.value) {
                    TodayQuestionDialog(
                        onPositiveClick = { navController.navigate(BottomNavItem.Question.screenRoute) },
                        onDismissRequest = { visibleMoveToQuestionDialogState.value = false }
                    )
                }

                /** ???????????? ??????????????? */
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

/** ????????? ???????????? ?????? ??????????????? */
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
            /** ????????? ???????????? ?????? ??????????????? ?????? */
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                /** ??????????????? ?????? */
                Text(text = "????????? ?????? ??????", style = MaterialTheme.typography.subtitle1)

                Spacer(modifier = Modifier.height(32.dp))

                /** ????????? */
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

                /** ????????? ?????? */
                Text(
                    text = "?????? ????????? ????????? ????????? ???????????????",
                    style = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.height(32.dp))

                /** ?????????????????? ?????? */
                RoundedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), label = "???????????? ??????"
                ) {
                    onPositiveClick()
                }
            }
        }
    }
}

/** ??? ?????? */
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

        /** ?????? */
        Row(verticalAlignment = Alignment.CenterVertically) {

            /** ?????? ????????? */
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Home,
                tint = Color.White,
                contentDescription = "icon home"
            )
            Spacer(modifier = Modifier.width(8.dp))

            /** ??? ?????? */
            Text(
                modifier = Modifier.offset(y = (-2).dp),
                text = "?????????",
                style = MaterialTheme.typography.subtitle1.copy(color = Color.White)
            )
        }

        /** ?????? ????????? */
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

/** ????????? ????????? */
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
                /** ????????? ????????? ????????? */
                PersonListItem(item, onImageClick)
            })
            item { }
        }
    }
}

/** ????????? ????????? ????????? */
@Composable
private fun PersonListItem(item: DomainUserDTO, onImageClick: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        /** ?????? */
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

        /** ?????? */
        Text(
            text = item.name,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}


/** ?????? ?????? */
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
            /** ?????? */
            Text(text = title, style = MaterialTheme.typography.subtitle2)
            Spacer(modifier = Modifier.height(12.dp))

            /** ?????? */
            Text(
                text = content,
                style = MaterialTheme.typography.body2.copy(
                    lineHeight = 20.sp,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            /** ????????? */
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

/** ?????? ?????? */
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
                /** ?????? ?????? ????????? ??? */
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(3.dp)
                        .background(Color.LightGray),
                ) {

                }

                Spacer(modifier = Modifier.height(16.dp))

                /** ????????? ?????? ?????? */
                if (list.isEmpty()) {
                    NoScheduleItem(onAddScheduleClick = onAddScheduleClick)
                }
                /** ?????? ?????? */
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

/** ????????? ?????? ?????? ???????????? */
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
        /** ?????? ?????? ?????? */
        Text(text = "????????? ??????", style = MaterialTheme.typography.subtitle2)

        /** ?????? ?????? ?????? ?????? */
        Text(
            modifier = Modifier.clickable {
                // ?????? ?????? ???????????? ??????
                onAddScheduleClick()
            },
            text = "?????? ??????",
            style = MaterialTheme.typography.body2.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    /** ?????? ?????? ?????? ????????? */
    TodayScheduleList(list = list, onScheduleClick = onScheduleClick)
}

/** ????????? ?????? ?????? ???????????? */
@Composable
private fun NoScheduleItem(onAddScheduleClick: () -> Unit) {
    /** ?????? */
    Text(
        text = "????????? ????????? ????????????",
        style = MaterialTheme.typography.subtitle2
    )

    Spacer(modifier = Modifier.height(16.dp))

    /** ?????? ????????? */
    Image(
        modifier = Modifier
            .size(112.dp)
            .clickable {
                // ?????? ?????? ???????????? ??????
                onAddScheduleClick()
            },
        painter = painterResource(id = R.drawable.ic_calendar_add),
        contentDescription = "calendar add"
    )

    Spacer(modifier = Modifier.height(16.dp))

    /** ?????? */
    Text(
        text = "????????? ???????????? ????????? ??????????????????!",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
    )
}

/** ?????? ?????? ???????????? ?????? ?????? */
fun moveToAddScheduleScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.AddScheduleScreen.name)
}

/** ???????????? ??????????????? ??? */
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
            /** ???????????? ??????????????? ?????? */
            InviteDialogContent(familyCode, onShareClick, onDismissRequest)
        }
    }
}

/** ???????????? ??????????????? ?????? */
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
        /** ??????????????? ?????? */
        Text(text = "????????? ??????", style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.height(32.dp))

        /** ????????? ?????? */
        Text(
            text = familyCode,
            style = MaterialTheme.typography.h2.copy(
                color = Color.Gray,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(60.dp))

        /** ???????????? ?????? */
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), label = "?????????????????? ??????"
        ) {
            onShareClick()
        }
        Text(
            text = "??????",
            style = MaterialTheme.typography.button.copy(color = Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDismissRequest() }
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

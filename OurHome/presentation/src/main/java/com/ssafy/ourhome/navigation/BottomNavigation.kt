package com.ssafy.ourhome.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(navController: NavController, bottomBarState: MutableState<Boolean>) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Question,
        BottomNavItem.Album,
        BottomNavItem.MyPage
    )

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            androidx.compose.material.BottomNavigation(
                backgroundColor = Color.White,
                contentColor = Color(0xFFFF924B)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route


                items.forEach { item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = stringResource(id = item.title),
                                modifier = Modifier
                                    .width(26.dp)
                                    .height(26.dp)
                            )
                        },
                        label = { Text(stringResource(id = item.title), fontSize = 9.sp) },
                        selectedContentColor = MaterialTheme.colors.primary,
                        unselectedContentColor = Color.Gray,
                        selected = currentRoute == item.screenRoute,
                        alwaysShowLabel = false,
                        onClick = {
                            if (navController.currentBackStackEntry?.destination?.route == item.screenRoute) return@BottomNavigationItem
                            navController.navigate(item.screenRoute) {
//                                navController.graph.startDestinationRoute?.let {
//                                    popUpTo(it) { saveState = true }
//                                }
                                navController.popBackStack(
                                    route = BottomNavItem.Home.screenRoute,
                                    inclusive = false
                                )
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    )
}
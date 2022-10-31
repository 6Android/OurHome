package com.ssafy.ourhome.navigation

import com.ssafy.ourhome.R
import com.ssafy.ourhome.utils.ALBUM
import com.ssafy.ourhome.utils.HOME
import com.ssafy.ourhome.utils.MYPAGE
import com.ssafy.ourhome.utils.QUESTION

sealed class BottomNavItem(
    val title: Int, val icon: Int, val screenRoute: String
) {
    object Home : BottomNavItem(R.string.bottom_home, R.drawable.ic_launcher_foreground, HOME)
    object Question :
        BottomNavItem(R.string.bottom_question, R.drawable.ic_launcher_background, QUESTION)

    object Album : BottomNavItem(R.string.bottom_album, R.drawable.ic_launcher_foreground, ALBUM)
    object MyPage : BottomNavItem(R.string.bottom_myPage, R.drawable.ic_launcher_background, MYPAGE)

}
package com.ssafy.ourhome.navigation

import java.lang.IllegalStateException

enum class OurHomeScreens {

    NextScreen,
    LoginScreen,
    JoinEmailScreen;

    companion object {
        fun fromRoute(route: String?): OurHomeScreens = when (route?.substringBefore("/")) {
            NextScreen.name -> NextScreen
            LoginScreen.name -> LoginScreen
            JoinEmailScreen.name -> JoinEmailScreen

            else -> throw IllegalStateException("Route $route is not recognized")
        }
    }
}
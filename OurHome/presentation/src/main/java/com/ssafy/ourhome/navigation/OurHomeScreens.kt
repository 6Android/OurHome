package com.ssafy.ourhome.navigation

import java.lang.IllegalStateException

enum class OurHomeScreens {

    NextScreen;

    companion object {
        fun fromRoute(route: String?): OurHomeScreens = when (route?.substringBefore("/")) {
            NextScreen.name -> NextScreen

            else -> throw IllegalStateException("Route $route is not recognized")
        }
    }
}
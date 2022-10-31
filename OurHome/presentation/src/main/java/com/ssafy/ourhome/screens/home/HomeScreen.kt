package com.ssafy.ourhome.screens.home

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ssafy.ourhome.navigation.OurHomeScreens

@Composable
fun HomeScreen(navController: NavController){
    Button(onClick = {
        navController.navigate(OurHomeScreens.NextScreen.name)
    }) {
        Text(text = "next")

    }
}
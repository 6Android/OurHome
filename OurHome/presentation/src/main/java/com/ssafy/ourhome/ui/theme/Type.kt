package com.ssafy.ourhome.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ssafy.ourhome.R

val hannar = FontFamily(
    Font(R.font.hannar, FontWeight.Normal),
)

val nanum = FontFamily(
    Font(R.font.nanumsquareround_l, FontWeight.Light),
    Font(R.font.nanumsquareround_r, FontWeight.Normal),
    Font(R.font.nanumsquareround_b, FontWeight.Bold),
    Font(R.font.nanumsquareround_eb, FontWeight.ExtraBold)
)

// Set of Material typography styles to start with
val Typography = Typography(

    /** FontWeight는 Normal **/
    h1 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        letterSpacing = 0.sp
    ),
    h6 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp
    ),



    /** 한나 **/
    subtitle1 = TextStyle(
        fontFamily = hannar,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = hannar,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    ),



    body1 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),


    button = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        fontFamily = hannar,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 1.5.sp
    )
)
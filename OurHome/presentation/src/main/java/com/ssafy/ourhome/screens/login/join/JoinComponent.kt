package com.ssafy.ourhome.screens.login.join

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.ui.theme.MainColor
import org.checkerframework.checker.units.qual.m

/** 다음 버튼 */
@Composable
fun NextButton(title: String, onClick: () -> Unit) {
    /** 다음 버튼 */
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp), contentAlignment = Alignment.BottomCenter
    ) {
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), label = title,
            onClick = onClick,

        )
    }
}

/** 인디케이터들 */
@Composable
fun JoinIndicator(step: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        for (i in 1..3) {
            if (i <= step) Indicator(MainColor)
            else Indicator()
        }
    }
}

/** 인디케이터 */
@Composable
private fun Indicator(color: Color = Color.LightGray) {
    Box(
        Modifier
            .padding(4.dp)
            .background(color = color, shape = CircleShape)
            .size(12.dp)
    ) {
    }
}
package com.ssafy.ourhome.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme


@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    label: String = "Button",
    radius: Int = 20,
    onClick: () -> Unit = {}
) {

    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(CornerSize(radius))
        ),
        color = MainColor
    ) {

        Column(
            modifier = modifier
                .width(90.dp)
                .heightIn(40.dp)
                .clickable { onClick.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.button)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreView() {
    OurHomeTheme {
        RoundedButton()
    }
}
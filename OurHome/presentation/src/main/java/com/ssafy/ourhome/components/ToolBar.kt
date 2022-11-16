package com.ssafy.ourhome.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun MainAppBar(
    title: String,
    backIconEnable: Boolean = true,
    icon: Painter? = null,
    onBackClick: () -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (backIconEnable) {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "BackButton",
                        modifier = Modifier.clickable {
                            onBackClick.invoke()
                        }
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = if (backIconEnable) 12.dp else 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Text(text = title, style = MaterialTheme.typography.subtitle2)

                    if (icon != null) {
                        Icon(
                            painter = icon,
                            contentDescription = "IconButton",
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable {
                                    onIconClick.invoke()
                                }
                                .size(32.dp)
                        )
                    }
                }
            }
        },
        backgroundColor = Color.White
    )
}
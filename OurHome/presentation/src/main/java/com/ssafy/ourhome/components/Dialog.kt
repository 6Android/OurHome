package com.ssafy.ourhome.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ssafy.ourhome.ui.theme.RED


/** 경고 다이얼로그 창 */
@Composable
fun AlertDialog(
    onConfirmClick: () -> Unit = {},
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

            AlertDialogContent("정말로 삭제하시겠습니까?", "삭제", onConfirmClick, onDismissRequest)
        }
    }
}

@Composable
fun AlertDialogContent(
    header: String,
    confirmText: String,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        /** 다이얼로그 헤더 */
        /** 다이얼로그 헤더 */
        Text(text = header, style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            AlertRoundedButton(
                modifier = Modifier.weight(1F), label = confirmText
            ) {
                onConfirmClick()
            }
            Text(
                text = "닫기",
                style = MaterialTheme.typography.button.copy(color = Color.Gray),
                modifier = Modifier.weight(1F)
                    .clickable { onDismissRequest() },
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun AlertRoundedButton(
    modifier: Modifier = Modifier,
    label: String = "Button",
    radius: Int = 20,
    onClick: () -> Unit = {}
) {

    Surface(
        modifier = modifier.clip(
            RoundedCornerShape(CornerSize(radius))
        ),
        color = RED
    ) {

        Column(
            modifier = modifier
                .heightIn(40.dp)
                .clickable { onClick.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.button, color = Color.White)
        }
    }
}
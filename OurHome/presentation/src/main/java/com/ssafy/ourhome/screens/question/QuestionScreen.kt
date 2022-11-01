package com.ssafy.ourhome.screens.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.ui.theme.nanum

@Composable
fun QuestionScreen(navController : NavController) {
    val painter = rememberAsyncImagePainter("https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg")
    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "고라파덕",
            style = MaterialTheme.typography.h5
        )
        Image(
            modifier = Modifier.size(250.dp),
            painter = painter,
            contentDescription = "펫 이미지"
        )
        Text(
            text = "Lv.2",
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MainColor,
                        fontFamily = nanum,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 34.sp
                    )
                ) {
                    append("Q4. ")
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = nanum,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp
                    )
                ) {
                    append("오늘 점심 뭐 드셨나요?")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        QuestionButton(modifier = Modifier
            .width(150.dp)
            .height(50.dp),
            label = "답변 하기")

    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OurHomeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = rememberNavController()
            QuestionScreen(navController)
        }
    }
}


@Composable
fun QuestionButton(
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
            Text(text = label, style = TextStyle(
                fontFamily = nanum,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                letterSpacing = 0.15.sp
            ))
        }
    }
}
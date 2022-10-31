package com.ssafy.ourhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.ourhome.components.EditTextPreView
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.ui.theme.OurHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OurHomeTheme {
//                MyApp()
                EditTextPreView()
            }
        }
    }
}

@Composable
fun MyApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            RoundedButton(label = "우리집")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OurHomeTheme {
        MyApp()
    }
}
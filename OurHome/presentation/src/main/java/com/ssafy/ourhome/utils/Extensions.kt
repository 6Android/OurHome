package com.ssafy.ourhome.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.*
import com.kizitonwose.calendar.compose.CalendarState
import kotlinx.coroutines.flow.filter
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

/** 캘린더 */
@Composable
fun rememberFirstVisibleMonthAfterScroll(state: CalendarState): YearMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth.yearMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleMonth.value = state.firstVisibleMonth.yearMonth }
    }
    return visibleMonth.value
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.year}년 ${this.month.displayText(short = short)} 가족일정"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.KOREA)
}

/** 에딧텍스트 포커스 관리 함수 (키보드 닫힘사용) **/
fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}
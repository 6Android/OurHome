package com.ssafy.ourhome.components.pie.renderer

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.ssafy.ourhome.components.pie.PieChartData

interface SliceDrawer {
    fun drawSlice(
        drawScope: DrawScope,
        canvas: Canvas,
        area: Size,
        startAngle: Float,
        sweepAngle: Float,
        slice: PieChartData.Slice
    )
}
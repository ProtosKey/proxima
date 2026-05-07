package interpolation.app.view.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import interpolation.app.data.MainStore
import interpolation.app.data.model.FunctionResult
import interpolation.app.domain.math.toDouble

@Composable
fun ApproximationChart(modifier: Modifier = Modifier) {
    val points by MainStore.points.collectAsStateWithLifecycle()
    val results by MainStore.results.collectAsStateWithLifecycle()

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val pad = 48.dp.toPx()

        val allX = points.map { it.x.toDouble() }
        val allY = points.map { it.y.toDouble() }
        val xMin = (allX.minOrNull() ?: -10.0) - 1.0
        val xMax = (allX.maxOrNull() ?: 10.0) + 1.0
        val yMin = (allY.minOrNull() ?: -10.0) - 1.0
        val yMax = (allY.maxOrNull() ?: 10.0) + 1.0

        fun toScreenX(x: Double) =
            pad + ((x - xMin) / (xMax - xMin) * (w - 2 * pad)).toFloat()
        fun toScreenY(y: Double) =
            (h - pad) - ((y - yMin) / (yMax - yMin) * (h - 2 * pad)).toFloat()

        // Сетка
        val gridColor = Color.Gray.copy(alpha = 0.2f)
        for (i in 0..8) {
            val x = pad + i * (w - 2 * pad) / 8
            val y = pad + i * (h - 2 * pad) / 8
            drawLine(gridColor, Offset(x, pad), Offset(x, h - pad), strokeWidth = 1.dp.toPx())
            drawLine(gridColor, Offset(pad, y), Offset(w - pad, y), strokeWidth = 1.dp.toPx())
        }

        // Оси
        val axisColor = Color.Gray.copy(alpha = 0.5f)
        val zeroX = toScreenX(0.0).coerceIn(pad, w - pad)
        val zeroY = toScreenY(0.0).coerceIn(pad, h - pad)
        drawLine(axisColor, Offset(pad, zeroY), Offset(w - pad, zeroY), strokeWidth = 1.5.dp.toPx())
        drawLine(axisColor, Offset(zeroX, pad), Offset(zeroX, h - pad), strokeWidth = 1.5.dp.toPx())

        // Кривые
        results.forEach { (_, result) ->
            if (result is FunctionResult.Success) {
                val pts = (0..200).mapNotNull { i ->
                    val x = xMin + i * (xMax - xMin) / 200
                    try {
                        val y = result.function.calculate(x.toBigDecimal()).toDouble()
                        if (y.isFinite()) Offset(toScreenX(x), toScreenY(y)) else null
                    } catch (_: Exception) { null }
                }
                for (i in 1 until pts.size) {
                    drawLine(Color(0xFF4A90E2), pts[i - 1], pts[i], strokeWidth = 2.dp.toPx())
                }
            }
        }

        // Точки
        points.forEach { point ->
            val sx = toScreenX(point.x.toDouble())
            val sy = toScreenY(point.y.toDouble())
            drawCircle(Color(0xFFE2574A), radius = 5.dp.toPx(), center = Offset(sx, sy))
            drawCircle(Color.White, radius = 2.5.dp.toPx(), center = Offset(sx, sy))
        }
    }
}
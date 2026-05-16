package interpolation.app.view.feature.graph.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import interpolation.app.data.model.FunctionType
import interpolation.app.presentation.state.GraphState
import kotlin.collections.associateWith
import kotlin.math.*

@Composable
fun Graph(
    state: GraphState,
    onClick: (x: Float, y: Float) -> Unit,
    onRange: (minX: Float, maxX: Float, minY: Float, maxY: Float) -> Unit,
    modifier: Modifier = Modifier,
    theBest: FunctionType?
) {
    val colors = MaterialTheme.colorScheme
    val curveColors = remember(colors, theBest) {
        FunctionType.entries.associateWith {
            if (theBest != null && it == theBest)
                colors.primary
            else
                colors.outlineVariant
        }
    }

    val density = LocalDensity.current
    val widthByTypes = remember(theBest) {
        FunctionType.entries.associateWith {
            if (theBest != null && it == theBest)
                with(density) { 3.dp.toPx() }
            else
                with(density) { 2.dp.toPx() }
        }
    }

    var zoom by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        zoom = (zoom * zoomChange).coerceIn(0.1f, 30f)
        panOffset += panChange
    }

    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(
        color = colors.onSurfaceVariant.copy(alpha = 0.5f),
        fontSize = MaterialTheme.typography.bodySmall.fontSize
    )

    val axisLabelStyle = TextStyle(
        color = colors.onSurfaceVariant, fontSize = MaterialTheme.typography.bodyLarge.fontSize
    )

    Canvas(
        modifier = modifier.fillMaxSize().clipToBounds().transformable(transformableState)
    ) {
        val padding = 80f
        val drawWidth = size.width - padding * 2
        val drawHeight = size.height - padding * 2

        val view = state.viewport
        val mathWidth = (view.maxHorizon - view.horizon).coerceAtLeast(0.1f)
        val mathHeight = (view.maxVertical - view.vertical).coerceAtLeast(0.1f)

        fun toOffset(mathX: Float, mathY: Float): Offset {
            val xProgress = (mathX - view.horizon) / mathWidth
            val yProgress = (view.maxVertical - mathY) / mathHeight
            return Offset(
                x = (padding + xProgress * drawWidth) * zoom + panOffset.x,
                y = (padding + yProgress * drawHeight) * zoom + panOffset.y
            )
        }

        fun toMath(offset: Offset): Offset {
            val mx =
                ((offset.x - panOffset.x) / zoom - padding) / drawWidth * mathWidth + view.horizon
            val my =
                view.maxVertical - (((offset.y - panOffset.y) / zoom - padding) / drawHeight * mathHeight)
            return Offset(mx, my)
        }

        val visibleTopLeft = toMath(Offset.Zero)
        val visibleBottomRight = toMath(Offset(size.width, size.height))

        val minVisibleX = min(visibleTopLeft.x, visibleBottomRight.x)
        val maxVisibleX = max(visibleTopLeft.x, visibleBottomRight.x)
        val minVisibleY = min(visibleTopLeft.y, visibleBottomRight.y)
        val maxVisibleY = max(visibleTopLeft.y, visibleBottomRight.y)

        val zeroPos = toOffset(0f, 0f)

        drawGrid(
            minHorizon = minVisibleX,
            maxHorizon = maxVisibleX,
            minVertical = minVisibleY,
            maxVertical = maxVisibleY,
            color = colors.outlineVariant.copy(alpha = .4f),
            measurer = textMeasurer,
            style = labelStyle,
            offset = ::toOffset
        )

        drawLine(
            colors.outlineVariant,
            Offset(zeroPos.x, 0f),
            Offset(zeroPos.x, size.height),
            1.dp.toPx()
        )
        drawLine(
            colors.outlineVariant, Offset(0f, zeroPos.y), Offset(size.width, zeroPos.y), 1.dp.toPx()
        )

        drawText(textMeasurer, "Y", Offset(zeroPos.x + 10f, 10f), axisLabelStyle)
        drawText(textMeasurer, "X", Offset(size.width - 30f, zeroPos.y - 50f), axisLabelStyle)

        state.graph.filter { (key, _) ->
            (state.visible[key] ?: false)
        }.forEach { (key, points) ->
            val path = Path()
            var first = true
            points.forEach { p ->
                val pos = toOffset(p.x, p.y)
                if (pos.x in -1000f..size.width + 1000f && pos.y in -1000f..size.height + 1000f) {
                    if (first) path.moveTo(pos.x, pos.y) else path.lineTo(pos.x, pos.y)
                    first = false
                }
            }
            drawPath(
                path = path,
                color = curveColors[key] ?: colors.outline,
                style = Stroke(
                    width = widthByTypes[key] ?: 2.dp.toPx()
                )
            )
        }

        state.points.forEach { point ->
            val center = toOffset(point.x, point.y)
            drawCircle(color = colors.primary, radius = 18f, center = center)
            drawCircle(color = colors.background, radius = 12f, center = center)
        }
    }
}

private fun DrawScope.drawGrid(
    minHorizon: Float,
    maxHorizon: Float,
    minVertical: Float,
    maxVertical: Float,
    color: Color,
    measurer: TextMeasurer,
    style: TextStyle,
    offset: (Float, Float) -> Offset
) {
    val horizon = getGripStep((maxHorizon - minHorizon) / 6)
    val vertical = getGripStep((maxVertical - minVertical) / 6)

    val start = floor(minHorizon / horizon) * horizon
    var current = start
    while (current <= maxHorizon + horizon) {
        val value = offset(current, 0f).x
        if (value in 0f..size.width) {
            drawLine(
                color = color,
                start = Offset(value, 0f),
                end = Offset(value, size.height),
                strokeWidth = 1.dp.toPx()
            )
            drawText(
                textMeasurer = measurer,
                text = format(current),
                style = style,
                topLeft = Offset(value + 15f, size.height - 50f)
            )
        }
        current += horizon
    }

    val startVertical = floor(minVertical / vertical) * vertical
    var currentVertical = startVertical
    while (currentVertical <= maxVertical + vertical) {
        val canvasY = offset(0f, currentVertical).y
        if (canvasY in 0f..size.height) {
            drawLine(
                color = color, Offset(0f, canvasY), Offset(size.width, canvasY), 1.dp.toPx()
            )
            drawText(
                textMeasurer = measurer,
                text = format(currentVertical),
                style = style,
                topLeft = Offset(20f, canvasY + 10f)
            )
        }
        currentVertical += vertical
    }
}

private fun getGripStep(step: Float): Float {
    if (step <= 0f) return 1f
    val magnitude = 10.0.pow(floor(log10(step.toDouble()))).toFloat()
    val res = step / magnitude
    return magnitude * when {
        res < 1.5f -> 1f
        res < 3.5f -> 2f
        res < 7.5f -> 5f
        else -> 10f
    }
}

private fun format(value: Float): String {
    if (abs(value) < 1e-5) return "0"
    return if (value == floor(value.toDouble()).toFloat()) {
        value.toInt().toString()
    } else {
        (round(value * 100) / 100.0).toString()
    }
}
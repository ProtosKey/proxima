package interpolation.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import interpolation.app.data.MainStore
import interpolation.app.data.model.FunctionResult
import interpolation.app.data.model.FunctionType
import interpolation.app.data.model.MessageType
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function
import interpolation.app.presentation.basic.HaveMessage
import interpolation.app.presentation.mapper.DataMapper
import interpolation.app.presentation.model.PointData
import interpolation.app.presentation.state.GraphState
import interpolation.app.presentation.state.Viewport
import interpolation.app.presentation.tools.FastCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GraphViewModel : ViewModel(), HaveMessage {
    companion object {
        private const val CURVE_POINTS = 300
    }

    private val _graphState = MutableStateFlow(GraphState())
    val graphState = _graphState.asStateFlow()
    val notification = MainStore.notification

    init {
        combine(
            MainStore.points,
            MainStore.results,
            MainStore.visibleResults
        ) { points, results, visible ->
            val pointData = points.map(DataMapper::mapTo)
            val viewport = calcViewport(pointData)

            val curves = withContext(Dispatchers.Default) {
                results
                    .filterValues { it is FunctionResult.Success }
                    .mapValues { (_, result) ->
                        buildCurves(
                            function = (result as FunctionResult.Success).function,
                            left = viewport.left,
                            right = viewport.right
                        )
                    }
            }

            _graphState.update {
                it.copy(
                    points = pointData,
                    graph = curves,
                    canAdd = points.size < Coordinates.MAX_SIZE,
                    viewport = viewport,
                    visible = visible,
                    theBest = findBestResult(MainStore.results.value)
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun findBestResult(result: Map<FunctionType, FunctionResult>): FunctionType? {
        return result.filterValues { result ->
            result is FunctionResult.Success
        }.maxByOrNull { result ->
            (result.value as FunctionResult.Success).metrics.determination
        }?.key
    }

    private fun calcViewport(points: List<PointData>): Viewport {
        if (points.isEmpty()) return Viewport()

        val horizon = points.minOf { it.x }
        val maxHorizon = points.maxOf { it.x }
        val vertical = points.minOf { it.y }
        val maxVertical = points.maxOf { it.y }

        val paddingHorizon = (maxHorizon - horizon).coerceAtLeast(1f) * .1f
        val paddingVertical = (maxVertical - vertical).coerceAtLeast(1f) * .1f

        return Viewport(
            horizon = horizon - paddingHorizon,
            maxHorizon = maxHorizon + paddingHorizon,
            vertical = vertical - paddingVertical,
            maxVertical = maxVertical + paddingVertical
        )
    }

    fun updateVisible(
        left: Float,
        right: Float,
        bottom: Float,
        top: Float
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val viewport = Viewport(
                horizon = left,
                maxHorizon = right,
                vertical = bottom,
                maxVertical = top
            )

            val results = MainStore.results.value
            val curves = results
                .filterValues { it is FunctionResult.Success }
                .mapValues { (_, result) ->
                    buildCurves(
                        function = (result as FunctionResult.Success).function,
                        left = viewport.left,
                        right = viewport.right
                    )
                }
            _graphState.update {
                it.copy(
                    graph = curves,
                    viewport = viewport
                )
            }
        }
    }

    private fun buildCurves(
        function: Function,
        left: Float,
        right: Float
    ): List<PointData> {
        val fast = function.acceptVisitor(FastCalculator)
        val step = (right - left) / CURVE_POINTS

        return (0..CURVE_POINTS).mapNotNull { i ->
            val x = left + i * step
            try {
                PointData(x, fast.calculate(x.toDouble()).toFloat())
            } catch (_: Exception) {
                null
            }
        }
    }

    fun addPoint(x: Float, y: Float) {
        if (MainStore.points.value.size < Coordinates.MAX_SIZE) {
            val point = DataMapper.mapFrom(PointData(x, y))
            _graphState.update {
                it.copy(
                    canAdd = MainStore.points.value.size < Coordinates.MAX_SIZE
                )
            }
            MainStore.updatePoints(MainStore.points.value + point)
        } else {
            showMessage(
                "Превышено допустимое количество точек в ${Coordinates.MAX_SIZE} единиц",
                MessageType.ERROR
            )
        }
    }

    override fun hideMessage() {
        MainStore.hideMessage()
    }

    override fun showMessage(message: String, messageType: MessageType) {
        MainStore.showMessage(message, messageType)
    }
}

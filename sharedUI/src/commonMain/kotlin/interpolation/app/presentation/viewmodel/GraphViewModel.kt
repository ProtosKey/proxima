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
import interpolation.app.presentation.tools.FastCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GraphViewModel : ViewModel(), HaveMessage {
    companion object {
        private const val CURVE_POINTS = 500
    }

    private var messageJob: Job? = null
    private var rangeJob: Job? = null
    private val _graphState = MutableStateFlow(GraphState())
    val graphState = _graphState.asStateFlow()
    val notification = MainStore.notification

    init {
        combine(
            MainStore.points,
            MainStore.visibleResults,
            MainStore.isLoading
        ) { points, visible, isLoading ->
            val pointData = points.map(DataMapper::mapTo)

            _graphState.update {
                it.copy(
                    points = pointData,
                    canAdd = points.size < Coordinates.MAX_SIZE,
                    visible = visible,
                    theBest = findBestResult(MainStore.results.value),
                    isLoading = isLoading
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

    fun updateVisible(
        left: Float,
        right: Float,
    ) {
        rangeJob?.cancel()
        rangeJob = viewModelScope.launch(Dispatchers.Default) {
            val results = MainStore.results.value
            val curves = results
                .filterValues { it is FunctionResult.Success }
                .mapValues { (_, result) ->
                    buildCurves(
                        function = (result as FunctionResult.Success).function,
                        left = left - (right - left),
                        right = right + (right - left)
                    )
                }

            _graphState.update { it.copy(graph = curves) }
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
            if (MainStore.points.value.size == Coordinates.MAX_SIZE) {
                showMessage(
                    "Добавлено максимальное количество точек в ${Coordinates.MAX_SIZE} единиц",
                    MessageType.WARNING
                )
            }
        } else {
            showMessage(
                "Превышено допустимое количество точек в ${Coordinates.MAX_SIZE} единиц",
                MessageType.ERROR
            )
        }
    }

    override fun hideMessage() {
        messageJob?.cancel()
        MainStore.hideMessage()
    }

    override fun showMessage(message: String, messageType: MessageType) {
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            if (MainStore.notification.value.isVisible) {
                MainStore.hideMessage()
                delay(250)
            }
            MainStore.showMessage(message, messageType)
        }
    }

    override fun onCleared() {
        super.onCleared()
        messageJob?.cancel()
        rangeJob?.cancel()
    }
}

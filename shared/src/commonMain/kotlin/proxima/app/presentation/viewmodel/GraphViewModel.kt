package proxima.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import proxima.app.data.MainStore
import proxima.app.data.model.FunctionResult
import proxima.app.data.model.findBest
import proxima.app.data.model.MessageType
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function
import proxima.app.presentation.basic.BaseViewModel
import proxima.app.presentation.mapper.DataMapper
import proxima.app.presentation.mapper.RawMapper
import proxima.app.presentation.model.PointData
import proxima.app.presentation.state.GraphState
import proxima.app.presentation.tools.FastCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GraphViewModel(store: MainStore) : BaseViewModel(store) {
    private var curvePoints: Int = 0
    private var rangeJob: Job? = null
    private val _graphState = MutableStateFlow(GraphState())
    val graphState = _graphState.asStateFlow()
    val notification = store.notification

    init {
        store.settings.onEach { settings ->
            curvePoints = settings.graphResolution.value.toInt()
        }.launchIn(viewModelScope)

        combine(
            store.rawPoints,
            store.visibleResults,
            store.isLoading,
            store.results
        ) { rawPoints, visible, isLoading, _ ->
            val pointData = rawPoints.mapNotNull { raw ->
                runCatching { DataMapper.mapTo(RawMapper.toPoint(raw)) }.getOrNull()
            }

            _graphState.update {
                it.copy(
                    points = pointData,
                    canAdd = rawPoints.size < Coordinates.MAX_SIZE,
                    visible = visible,
                    theBest = store.results.value.findBest(),
                    isLoading = isLoading
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateVisible(
        left: Float,
        right: Float,
    ) {
        rangeJob?.cancel()
        rangeJob = viewModelScope.launch(Dispatchers.Default) {
            val results = store.results.value
            val curves = results
                .filterValues { it is FunctionResult.Success }
                .mapValues { (key, result) ->
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
        val step = (right - left) / curvePoints

        return (0..curvePoints).mapNotNull { i ->
            val x = left + i * step
            try {
                PointData(x, fast.calculate(x.toDouble()).toFloat())
            } catch (_: Exception) {
                null
            }
        }
    }

    fun addPoint(x: Float, y: Float) {
        if (store.rawPoints.value.size < Coordinates.MAX_SIZE) {
            val raw = RawMapper.toRaw(DataMapper.mapFrom(PointData(x, y)))
            val newSize = store.addRawPoint(raw)
            _graphState.update {
                it.copy(canAdd = newSize < Coordinates.MAX_SIZE)
            }
            if (newSize == Coordinates.MAX_SIZE) {
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

    override fun onCleared() {
        super.onCleared()
        rangeJob?.cancel()
    }
}

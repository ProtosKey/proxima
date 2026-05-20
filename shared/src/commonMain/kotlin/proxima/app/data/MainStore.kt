package proxima.app.data

import proxima.app.data.model.FunctionResult
import proxima.app.data.model.FunctionType
import proxima.app.data.model.MessageType
import proxima.app.data.model.Notification
import proxima.app.data.model.Settings
import proxima.app.domain.model.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainStore {
    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points = _points.asStateFlow()

    private val _visibleResults = MutableStateFlow<Map<FunctionType, Boolean>>(emptyMap())
    val visibleResults = _visibleResults.asStateFlow()

    private val _results = MutableStateFlow<Map<FunctionType, FunctionResult>>(emptyMap())
    val results = _results.asStateFlow()

    private val _notification = MutableStateFlow(Notification())
    val notification = _notification.asStateFlow()

    private val _settings = MutableStateFlow(Settings())
    val settings = _settings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun checkVisible() {
        _visibleResults.update {
            _results.value.mapValues { (key, _) ->
                _visibleResults.value[key] ?: true
            }
        }
    }

    fun tickVisible(type: FunctionType) {
        _visibleResults.update { current ->
            current.toMutableMap().apply {
                this[type] = !(_visibleResults.value[type] ?: false)
            }
        }
    }

    fun startLoading() {
        _isLoading.update { true }
    }

    fun endLoading() {
        _isLoading.update { false }
    }

    fun deletePointByIndex(index: Int) {
        _points.update { points ->
            val newPoints = points.toMutableList()
            if (index in newPoints.indices) {
                newPoints.removeAt(index)
            }
            newPoints
        }
    }

    fun updateMathPrecision(key: String) {
        _settings.update {
            it.copy(mathPrecision = it.mathPrecision.copy(current = key))
        }
    }

    fun updateGraphResolution(key: String) {
        _settings.update {
            it.copy(graphResolution = it.graphResolution.copy(current = key))
        }
    }

    fun updateDisplayPrecision(key: String) {
        _settings.update {
            it.copy(displayPrecision = it.displayPrecision.copy(current = key))
        }
    }

    fun addPoint(point: Point): Int {
        var newSize = 0
        _points.update { current ->
            val updated = current + point
            newSize = updated.size
            updated
        }
        return newSize
    }

    fun updatePoints(points: List<Point>) {
        _points.update { points }
    }

    fun updateFunctions(results: Map<FunctionType, FunctionResult>) {
        _results.update { results }
        checkVisible()
    }

    fun showMessage(message: String, messageType: MessageType) {
        _notification.update {
            it.copy(
                message = message, messageType = messageType, isVisible = true
            )
        }
    }

    fun hideMessage() {
        _notification.update {
            it.copy(
                isVisible = false
            )
        }
    }
}

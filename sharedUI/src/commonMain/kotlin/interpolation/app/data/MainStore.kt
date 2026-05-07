package interpolation.app.data

import interpolation.app.data.model.FunctionResult
import interpolation.app.data.model.FunctionType
import interpolation.app.data.model.MessageType
import interpolation.app.data.model.Notification
import interpolation.app.data.model.Settings
import interpolation.app.domain.model.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object MainStore {
    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points = _points.asStateFlow()

    private val _results = MutableStateFlow<Map<FunctionType, FunctionResult>>(emptyMap())
    val results = _results.asStateFlow()

    private val _notification = MutableStateFlow(Notification())
    val notification = _notification.asStateFlow()

    private val _settings = MutableStateFlow(Settings())
    val settings = _settings.asStateFlow()

    fun updateSettingsNewPoints(isNewPoints: Boolean) {
        _settings.update { it.copy(isNewPoints = isNewPoints) }
    }

    fun updatePoints(points: List<Point>) {
        _points.value = points
    }

    fun updateFunctions(results: Map<FunctionType, FunctionResult>) {
        _results.value = results
    }

    fun showMessage(message: String, messageType: MessageType) {
        _notification.update {
            it.copy(
                message = message, messageType = messageType
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

package interpolation.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import interpolation.app.data.MainStore
import interpolation.app.data.model.MessageType
import interpolation.app.data.utils.Defaults
import interpolation.app.domain.model.Coordinates
import interpolation.app.presentation.basic.HaveMessage
import interpolation.app.presentation.exception.ModelException
import interpolation.app.presentation.exception.ParserException
import interpolation.app.presentation.mapper.EntryMapper
import interpolation.app.presentation.model.PointEntry
import interpolation.app.presentation.state.InputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class InputViewModel : ViewModel(), HaveMessage {
    private val _inputState = MutableStateFlow(InputState())
    val inputState = _inputState.asStateFlow()
    val notification = MainStore.notification

    init {
        MainStore.points.onEach { points ->
            if (points.size != _inputState.value.input.size) {
                _inputState.update {
                    it.copy(
                        input = points.map(EntryMapper::mapTo).toList(),
                        canDelete = points.size > Coordinates.MIN_SIZE,
                        canAdd = points.size < Coordinates.MAX_SIZE
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun hideMessage() {
        MainStore.hideMessage()
    }

    override fun showMessage(message: String, messageType: MessageType) {
        MainStore.showMessage(message, messageType)
    }

    fun updatePoint(index: Int, x: String, y: String) {
        try {
            checkIndex(index)
            val newInput = _inputState.value.input.toMutableList()
            newInput[index] = PointEntry(x, y)
            _inputState.update { it.copy(input = newInput) }
            parsePoints()
        } catch (e: ModelException) {
            showMessage(e.message ?: Defaults.message(), MessageType.ERROR)
        }
    }

    /*
    fun addPoint(x: String, y: String) {
        if (_inputState.value.input.size < Coordinates.MAX_SIZE) {
            _inputState.update {
                it.copy(
                    input = _inputState.value.input + PointEntry(x, y),
                    canDelete = _inputState.value.input.size > Coordinates.MIN_SIZE,
                    canAdd = _inputState.value.input.size + 1 < Coordinates.MAX_SIZE
                )
            }
            parsePoints()
        } else {
            showMessage(
                "Превышено допустимое количество точек в ${Coordinates.MAX_SIZE} единиц",
                MessageType.ERROR
            )
        }
    }
     */

    fun addPoint() {
        val point = Defaults.randomPoint()
        if (_inputState.value.input.size < Coordinates.MAX_SIZE) {
            _inputState.update {
                it.copy(
                    input = _inputState.value.input + EntryMapper.mapTo(point),
                    canDelete = _inputState.value.input.size + 1 > Coordinates.MIN_SIZE,
                    canAdd = _inputState.value.input.size + 1 < Coordinates.MAX_SIZE
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

    fun removeByIndex(index: Int) {
        try {
            checkIndex(index)
            MainStore.deletePointByIndex(index)
        } catch (e: ModelException) {
            showMessage(e.message ?: Defaults.message(), MessageType.ERROR)
        }
    }

    private fun parsePoints() {
        var needUpdate = true
        val points = _inputState.value.input.mapNotNull { point ->
            try {
                EntryMapper.mapFrom(point)
            } catch (_: Exception) {
                needUpdate = false
                null
            }
        }

        if (needUpdate)
            MainStore.updatePoints(points)
    }

    private fun checkIndex(index: Int) {
        if (index < 0 || index >= _inputState.value.input.size)
            throw ModelException("Точки с таким индексом не существует")
    }
}

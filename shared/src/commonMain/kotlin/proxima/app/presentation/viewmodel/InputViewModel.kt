package proxima.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import proxima.app.data.MainStore
import proxima.app.data.model.MessageType
import proxima.app.data.model.RawPoint
import proxima.app.data.utils.Defaults
import proxima.app.domain.model.Coordinates
import proxima.app.domain.utils.PointFactory
import proxima.app.presentation.basic.BaseViewModel
import proxima.app.presentation.exception.ModelException
import proxima.app.presentation.mapper.RawMapper
import proxima.app.presentation.state.InputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class InputViewModel(store: MainStore) : BaseViewModel(store) {
    private val _inputState = MutableStateFlow(InputState())
    val inputState = _inputState.asStateFlow()
    val notification = store.notification

    init {
        store.rawPoints.onEach { rawPoints ->
            if (rawPoints.size != _inputState.value.input.size) {
                _inputState.update {
                    it.copy(
                        input = rawPoints.map(RawMapper::toEntry),
                        canAdd = rawPoints.size < Coordinates.MAX_SIZE
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updatePoint(index: Int, x: String, y: String) {
        try {
            checkIndex(index)
            val newInput = _inputState.value.input.toMutableList()
            newInput[index] = RawMapper.toEntry(RawPoint(x, y))
            _inputState.update { it.copy(input = newInput) }
            store.updateRawPoint(index, x, y)
        } catch (e: ModelException) {
            showMessage(e.message ?: Defaults.message(), MessageType.ERROR)
        }
    }

    fun addPoint() {
        if (store.rawPoints.value.size < Coordinates.MAX_SIZE) {
            val newSize = store.addRawPoint(RawMapper.toRaw(PointFactory.random()))
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

    fun removeByIndex(index: Int) {
        try {
            checkIndex(index)
            store.removeRawPoint(index)
        } catch (e: ModelException) {
            showMessage(e.message ?: Defaults.message(), MessageType.ERROR)
        }
    }

    private fun checkIndex(index: Int) {
        if (index < 0 || index >= _inputState.value.input.size)
            throw ModelException("Точки с таким индексом не существует")
    }
}

package interpolation.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import interpolation.app.data.MainStore
import interpolation.app.presentation.state.SettingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    init {
        MainStore.settings.onEach { currentSettings ->
            _state.update { it.copy(settings = currentSettings) }
        }.launchIn(viewModelScope)
    }

    fun updateMathPrecision(key: String) = MainStore.updateMathPrecision(key)
    fun updateGraphResolution(key: String) = MainStore.updateGraphResolution(key)
    fun updateDisplayPrecision(key: String) = MainStore.updateDisplayPrecision(key)
}

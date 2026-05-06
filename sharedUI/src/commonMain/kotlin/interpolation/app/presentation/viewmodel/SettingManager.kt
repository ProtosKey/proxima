package interpolation.app.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingManager : ViewModel() {
    var isDarkMode = mutableStateOf(false)
    var isAutoUpdate = mutableStateOf(false)
    var clearOnTouch = mutableStateOf(true)
}

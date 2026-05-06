package interpolation.app.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf

class SettingsManager {
    var isDarkMode = mutableStateOf(false)
    var isAutoUpdate = mutableStateOf(false)
    var clearOnTouch = mutableStateOf(true)
}

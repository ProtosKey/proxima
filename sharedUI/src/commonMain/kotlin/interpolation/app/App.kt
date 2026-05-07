package interpolation.app

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import interpolation.app.theme.AppTheme
import interpolation.app.view.screen.MainScreen

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    Navigator(MainScreen())
}

package interpolation.app.view.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import interpolation.app.data.MainStore
import interpolation.app.theme.LocalAppDimens
import interpolation.app.view.component.NavigationBar
import interpolation.app.view.component.Title
import interpolation.app.view.feature.settings.component.Parameter

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val settings by MainStore.settings.collectAsStateWithLifecycle()

        Scaffold(
            bottomBar = { NavigationBar(navigator) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(LocalAppDimens.current.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.paddingExtraLarge)
            ) {
                Title(label = "Настройки")

                Column(
                    verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.paddingMedium)
                ) {
                    Parameter(
                        label = settings.isNewPoints.label,
                        description = settings.isNewPoints.current(),
                        isChecked = settings.isNewPoints.parameter
                    ) { MainStore.updateSettingsNewPoints(it) }

                    Parameter(
                        label = settings.isAutoUpdate.label,
                        description = settings.isAutoUpdate.current(),
                        isChecked = settings.isAutoUpdate.parameter
                    ) { MainStore.updateSettingAutoUpdate(it) }
                }
            }
        }
    }
}

package interpolation.app.view.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import interpolation.app.view.component.ApproximationChart
import interpolation.app.view.component.NavigationBar

class MainScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            bottomBar = { NavigationBar(navigator) }
        ) { padding ->
            ApproximationChart(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        }
    }
}

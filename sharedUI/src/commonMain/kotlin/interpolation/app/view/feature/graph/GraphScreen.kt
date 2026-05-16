package interpolation.app.view.feature.graph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import interpolation.app.presentation.viewmodel.GraphViewModel
import interpolation.app.theme.LocalAppDimens
import interpolation.app.view.component.Message
import interpolation.app.view.component.NavigationBar
import interpolation.app.view.component.Title
import interpolation.app.view.feature.graph.component.Graph

class GraphScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = viewModel<GraphViewModel>()
        val state by viewModel.graphState.collectAsStateWithLifecycle()
        val message by viewModel.notification.collectAsStateWithLifecycle()

        Scaffold(
            bottomBar = { NavigationBar(navigator) }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = LocalAppDimens.current.paddingMedium)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.paddingSmall)
                ) {
                    Title(label = "Главная")

                    if (state.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Graph(
                                state = state,
                                onClick = viewModel::addPoint,
                                onRange = viewModel::updateVisible,
                                theBest = state.theBest
                            )
                        }
                    }
                }

                Message(
                    message = message.message,
                    isVisible = message.isVisible,
                    onClick = { viewModel.hideMessage() },
                    bottom = 0.dp,
                    type = message.messageType
                )
            }
        }
    }
}

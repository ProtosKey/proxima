package interpolation.app.view.feature.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import interpolation.app.presentation.viewmodel.ResultViewModel
import interpolation.app.theme.LocalAppDimens
import interpolation.app.view.component.NavigationBar
import interpolation.app.view.component.Title
import interpolation.app.view.feature.result.component.ResultLabel

class ResultsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = viewModel<ResultViewModel>()
        val state by viewModel.resultState.collectAsStateWithLifecycle()
        var height by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Scaffold(
            bottomBar = { NavigationBar(navigator) },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    modifier = Modifier.onGloballyPositioned { add ->
                        height = with(density) { add.size.height.toDp() }
                    },
                    onClick = { viewModel.calculateResult() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(
                        LocalAppDimens.current.radiusMedium
                    ),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp
                    )
                ) {
                    interpolation.app.view.component.Button(
                        icon = Icons.Default.QueryStats,
                        label = "Посчитать",
                        description = "Посчитать результат"
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = LocalAppDimens.current.paddingMedium)
            ) {
                Title(label = "Результаты")
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(
                        LocalAppDimens.current.paddingSmall
                    ),
                    contentPadding = PaddingValues(
                        top = LocalAppDimens.current.paddingSmall,
                        bottom = height + LocalAppDimens.current.paddingLarge
                    ),
                ) {
                    if (state.isLoading) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else {
                        items(
                            state.results.entries.toList()
                        ) { (type, result) ->
                            val isBest = type == state.best
                            ResultLabel(
                                label = type.label,
                                isTheBest = isBest,
                                result = result,
                            )
                        }
                    }
                }
            }
        }
    }
}

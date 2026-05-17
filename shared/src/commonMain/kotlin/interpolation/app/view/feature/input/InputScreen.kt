package interpolation.app.view.feature.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import interpolation.app.presentation.viewmodel.InputViewModel
import interpolation.app.theme.LocalAppDimens
import interpolation.app.view.basic.factory
import interpolation.app.view.component.Button
import interpolation.app.view.component.Message
import interpolation.app.view.component.NavigationBar
import interpolation.app.view.component.Title
import interpolation.app.view.feature.graph.component.Empty
import interpolation.app.view.feature.input.component.Point

class InputScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = viewModel<InputViewModel>(factory = factory { InputViewModel() })
        val state by viewModel.inputState.collectAsStateWithLifecycle()
        val message by viewModel.notification.collectAsStateWithLifecycle()
        var height by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Scaffold(
            bottomBar = { NavigationBar(navigator) },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    modifier = Modifier.onGloballyPositioned { add ->
                        height = with(density) { add.size.height.toDp() }
                    },
                    onClick = { viewModel.addPoint() },
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
                    Button(
                        icon = Icons.Default.Add,
                        label = "Добавить",
                        description = "Добавить новую точку"
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = LocalAppDimens.current.paddingMedium)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.paddingSmall)
                    ) {
                        Title(label = "Ввод точек")
                        if (state.input.isNotEmpty()) {
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
                                itemsIndexed(
                                    items = state.input,
                                    key = { index, _ -> index }
                                ) { index, point ->
                                    Point(
                                        index = index,
                                        point = point,
                                        canDelete = viewModel.inputState.value.canDelete,
                                        onUpdate = { index: Int, x: String, y: String ->
                                            viewModel.updatePoint(index, x, y)
                                        },
                                        onDelete = { index: Int -> viewModel.removeByIndex(index) }
                                    )
                                }
                            }
                        } else {
                            Empty(
                                help = "Нажмите добавить для начала работы",
                                icon = Icons.Default.Add
                            )
                        }
                    }
                }

                Message(
                    message = message.message,
                    isVisible = message.isVisible,
                    onClick = { viewModel.hideMessage() },
                    bottom = height,
                    type = message.messageType
                )
            }
        }
    }
}

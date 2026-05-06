package interpolation.app.presentation.state

import interpolation.app.presentation.model.PointEntry

data class InputState(
    val input: List<PointEntry> = emptyList(),
    val canDelete: Boolean = false,
    val canAdd: Boolean = true
)

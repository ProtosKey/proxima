package interpolation.app.presentation.state

import interpolation.app.data.model.FunctionType
import interpolation.app.presentation.model.PointData

data class GraphState(
    val points: List<PointData> = emptyList(),
    val visible: Map<FunctionType, Boolean> = emptyMap(),
    val graph: Map<FunctionType, List<PointData>> = emptyMap(),
    val canAdd: Boolean = false,
    val theBest: FunctionType? = null,
    val isLoading: Boolean = false
)

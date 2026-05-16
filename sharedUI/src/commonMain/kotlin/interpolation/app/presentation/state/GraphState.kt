package interpolation.app.presentation.state

import interpolation.app.data.model.FunctionType
import interpolation.app.presentation.model.PointData

data class GraphState(
    val points: List<PointData> = emptyList(),
    val visible: Map<FunctionType, Boolean> = emptyMap(),
    val graph: Map<FunctionType, List<PointData>> = emptyMap(),
    val canAdd: Boolean = false,
    val theBest: FunctionType? = null,
    val viewport: Viewport = Viewport()
)

data class Viewport(
    val horizon: Float = 0f,
    val maxHorizon: Float = 1f,
    val vertical: Float = 0f,
    val maxVertical: Float = 1f
) {
    val left get() = horizon - (maxHorizon - horizon)
    val right get() = maxHorizon + (maxHorizon - horizon)
}

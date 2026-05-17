package interpolation.app.presentation.state

import interpolation.app.data.model.FunctionResult
import interpolation.app.data.model.FunctionType
import interpolation.app.presentation.tools.FunctionFormatter

data class ResultState(
    val formatter: FunctionFormatter = FunctionFormatter(5),
    val results: Map<FunctionType, FunctionResult> = emptyMap(),
    val selected: Map<FunctionType, Boolean> = emptyMap(),
    val best: FunctionType? = null,
    val isLoading: Boolean = false,
    val count: Long = 80L,
)

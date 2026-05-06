package interpolation.app.data.model

import interpolation.app.domain.model.Function
import interpolation.app.domain.model.Metrics

sealed class FunctionResult {
    data class Success(val function: Function, val metrics: Metrics) : FunctionResult()
    data class Error(val message: String) : FunctionResult()
}

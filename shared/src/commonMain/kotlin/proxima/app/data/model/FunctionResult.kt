package proxima.app.data.model

import proxima.app.domain.model.Function
import proxima.app.domain.model.Metrics

sealed class FunctionResult {
    data class Success(val function: Function, val metrics: Metrics) : FunctionResult()
    data class Error(val message: String) : FunctionResult()
}

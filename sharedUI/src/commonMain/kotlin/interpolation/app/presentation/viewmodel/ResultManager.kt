package interpolation.app.presentation.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import interpolation.app.domain.exception.EngineException
import interpolation.app.domain.exception.FunctionException
import interpolation.app.domain.exception.InitException
import interpolation.app.domain.exception.PrecisionException
import interpolation.app.domain.exception.SolverException
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.precision.QualityAnalyzer
import interpolation.app.presentation.exception.ModelException
import interpolation.app.presentation.exception.ParserException
import interpolation.app.presentation.model.FunctionResult
import interpolation.app.presentation.model.FunctionType
import interpolation.app.presentation.model.MessageType
import interpolation.app.presentation.tools.Defaults

class ResultManager {
    val results = mutableStateMapOf<FunctionType, FunctionResult>()
    val message = mutableStateOf(Defaults.message())
    val type = mutableStateOf(Defaults.messageType())
    val isVisible = mutableStateOf(Defaults.visible())

    fun calcResult(points: Coordinates) {
        val newResults = mutableMapOf<FunctionType, FunctionResult>()
        Defaults.solvers().forEach { (type, solver) ->
            try {
                val function = solver.solve(points)
                val metrics = QualityAnalyzer.analise(function, points)
                newResults[type] = FunctionResult.Success(function, metrics)
            } catch (e: SolverException) {
                newResults[type] = FunctionResult.Error(e.message ?: Defaults.exception())
            } catch (e: PrecisionException) {
                newResults[type] = FunctionResult.Error(e.message ?: Defaults.exception())
            }
        }

        results.clear()
        results.putAll(newResults)
    }

    fun showStatus(message: String, type: MessageType) {
        this.message.value = message
        this.type.value = type
        isVisible.value = true
    }

    fun closeStatus() {
        isVisible.value = false
    }
}

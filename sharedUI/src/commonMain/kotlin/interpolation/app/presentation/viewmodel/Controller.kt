package interpolation.app.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import interpolation.app.domain.exception.EngineException
import interpolation.app.domain.exception.FunctionException
import interpolation.app.domain.exception.InitException
import interpolation.app.domain.exception.PrecisionException
import interpolation.app.domain.exception.SolverException
import interpolation.app.domain.model.Coordinates
import interpolation.app.presentation.exception.ModelException
import interpolation.app.presentation.exception.ParserException
import interpolation.app.presentation.model.MessageType
import interpolation.app.presentation.tools.Defaults

class Controller : ViewModel() {
    val settings = SettingsManager()
    val points = PointsManager()
    val result = ResultManager()

    val isLoading = mutableStateOf(false)

    suspend fun updateResults() {
        startCalculate {
            result.calcResult(points.points.value)
        }
    }

    suspend fun updateEntities() {
        startCalculate {
            points.parseEntries()
        }
    }

    private suspend fun startCalculate(block: suspend () -> Unit) {
        isLoading.value = true
        try {
            block()
        } catch (e: Exception) {
            result.showStatus(
                when (e) {
                    is ModelException,
                    is ParserException,
                    is EngineException,
                    is FunctionException,
                    is InitException,
                    is PrecisionException,
                    is SolverException -> e.message

                    else -> Defaults.exception()
                } ?: Defaults.exception(), MessageType.ERROR
            )
        } finally {
            isLoading.value = false
        }
    }
}

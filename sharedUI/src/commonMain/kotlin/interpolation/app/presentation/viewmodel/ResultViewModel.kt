package interpolation.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import interpolation.app.data.MainStore
import interpolation.app.data.model.FunctionResult
import interpolation.app.data.model.FunctionType
import interpolation.app.data.model.MessageType
import interpolation.app.data.utils.Defaults
import interpolation.app.domain.exception.EngineException
import interpolation.app.domain.exception.FunctionException
import interpolation.app.domain.exception.InitException
import interpolation.app.domain.exception.PrecisionException
import interpolation.app.domain.exception.SolverException
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.precision.QualityAnalyzer
import interpolation.app.presentation.basic.HaveMessage
import interpolation.app.presentation.state.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel(), HaveMessage {
    private val _resultState = MutableStateFlow(ResultState())
    val resultState = _resultState.asStateFlow()

    init {
        combine(
            MainStore.results,
            MainStore.visibleResults,
            MainStore.isLoading
        ) { results, visible, isLoading ->
            _resultState.update {
                it.copy(
                    results = results,
                    selected = visible,
                    best = findBestResult(results),
                    isLoading = isLoading
                )
            }
        }.launchIn(viewModelScope)
    }

    fun tickFunction(functionType: FunctionType) {
        MainStore.tickVisible(functionType)
    }

    fun calculateResult() {
        if (_resultState.value.isLoading) return
        viewModelScope.launch(Dispatchers.Default) {
            MainStore.startLoading()
            try {
                val points = Coordinates(MainStore.points.value.toMutableList())
                val newResults = Defaults.solvers().mapValues { (_, solver) ->
                    try {
                        val function = solver.solve(points)
                        FunctionResult.Success(function, QualityAnalyzer.analise(function, points))
                    } catch (e: Exception) {
                        FunctionResult.Error(
                            getMessageByError(e)
                        )
                    }
                }
                MainStore.updateFunctions(newResults)
            } catch (e: Exception) {
                showMessage(
                    getMessageByError(e), MessageType.ERROR
                )
            } finally {
                MainStore.endLoading()
            }
        }
    }

    private fun getMessageByError(e: Exception): String {
        return when (e) {
            is EngineException,
            is FunctionException,
            is InitException,
            is PrecisionException,
            is SolverException -> e.message

            else -> Defaults.message()
        } ?: Defaults.message()
    }

    private fun findBestResult(result: Map<FunctionType, FunctionResult>): FunctionType? {
        return result.filterValues { result ->
            result is FunctionResult.Success
        }.maxByOrNull { result ->
            (result.value as FunctionResult.Success).metrics.determination
        }?.key
    }

    override fun hideMessage() {
        MainStore.hideMessage()
    }

    override fun showMessage(message: String, messageType: MessageType) {
        MainStore.showMessage(message, messageType)
    }
}

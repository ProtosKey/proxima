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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel(), HaveMessage {
    private val _resultState = MutableStateFlow(ResultState())
    val resultState = _resultState.asStateFlow()

    init {
        MainStore.results.onEach { results ->
            _resultState.update {
                it.copy(
                    results = results,
                    /*selected = results.keys.associateWith { key ->
                        (results[key] is FunctionResult.Success)
                    },*/
                    best = findBestResult(results),
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun tickFunction(functionType: FunctionType) {
        val check = _resultState.value.results[functionType]
        if (check == null)
            showMessage(Defaults.exception(), MessageType.GOOD)
        else {
            if (check is FunctionResult.Success) {
                val selected = _resultState.value.selected.toMutableMap()
                selected[functionType] = !(selected[functionType] ?: true)
                _resultState.update { it.copy(selected = selected) }
            } else
                showMessage("Не удалось отобразить график", MessageType.ERROR)
        }
    }

    fun calculateResult() {
        if (_resultState.value.isLoading) return
        viewModelScope.launch(Dispatchers.Default) {
            _resultState.update { it.copy(isLoading = true) }
            try {
                val points = Coordinates(MainStore.points.value.toMutableList())
                val newResults = Defaults.solvers().mapValues { (key, solver) ->
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
                _resultState.update { it.copy(isLoading = false) }
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

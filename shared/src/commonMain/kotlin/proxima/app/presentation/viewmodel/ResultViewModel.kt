package proxima.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import proxima.app.data.MainStore
import proxima.app.data.model.FunctionResult
import proxima.app.data.model.FunctionType
import proxima.app.data.model.MessageType
import proxima.app.data.utils.Defaults
import proxima.app.domain.exception.EngineException
import proxima.app.domain.exception.FunctionException
import proxima.app.domain.exception.InitException
import proxima.app.domain.exception.PrecisionException
import proxima.app.domain.exception.SolverException
import proxima.app.domain.model.Coordinates
import proxima.app.domain.precision.QualityAnalyzer
import proxima.app.presentation.basic.HaveMessage
import proxima.app.presentation.mapper.EntryMapper
import proxima.app.presentation.state.ResultState
import proxima.app.presentation.tools.FunctionFormatter
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(private val store: MainStore) : ViewModel(), HaveMessage {
    private val _resultState = MutableStateFlow(ResultState())
    val resultState = _resultState.asStateFlow()
    val notification = store.notification

    init {
        store.settings.onEach { settings ->
            _resultState.update {
                it.copy(
                    count = settings.mathPrecision.value.toLong(),
                    formatter = FunctionFormatter(settings.displayPrecision.value.toInt())
                )
            }
        }.launchIn(viewModelScope)

        combine(
            store.results,
            store.visibleResults,
            store.isLoading
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
        store.tickVisible(functionType)
    }

    fun calculateResult() {
        if (_resultState.value.isLoading) return
        if (store.rawPoints.value.isEmpty()) {
            showMessage("Добавьте точки для расчёта", MessageType.ERROR)
            return
        }
        if (store.rawPoints.value.any { runCatching { EntryMapper.mapFrom(it) }.isFailure }) {
            showMessage("Исправьте некорректные точки перед расчётом", MessageType.ERROR)
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            store.startLoading()
            try {
                val points = Coordinates(
                    store.rawPoints.value
                        .map {
                            EntryMapper.mapFrom(it)
                        }
                        .toMutableList()
                )
                val newResults = coroutineScope {
                    Defaults.solvers().map { (type, solver) ->
                        async {
                            type to try {
                                val function = solver.solve(points, _resultState.value.count)
                                FunctionResult.Success(
                                    function,
                                    QualityAnalyzer.analise(function, points, _resultState.value.count)
                                )
                            } catch (e: Exception) {
                                FunctionResult.Error(getMessageByError(e))
                            }
                        }
                    }.awaitAll().toMap()
                }
                store.updateFunctions(newResults)
            } catch (e: Exception) {
                showMessage(
                    getMessageByError(e), MessageType.ERROR
                )
            } finally {
                store.endLoading()
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
        store.hideMessage()
    }

    override fun showMessage(message: String, messageType: MessageType) {
        store.showMessage(message, messageType)
    }
}

package proxima.app.domain.basic

import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function
import proxima.app.domain.model.Metrics

interface Analyzer {
    fun analise(function: Function, points: Coordinates, count: Long): Metrics
}

package interpolation.app.domain.basic

import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function
import interpolation.app.domain.model.Metrics

interface Analyzer {
    fun analise(function: Function, points: Coordinates, count: Long): Metrics
}

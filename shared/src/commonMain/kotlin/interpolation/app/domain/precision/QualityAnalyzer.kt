package interpolation.app.domain.precision

import interpolation.app.domain.basic.Analyzer
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function
import interpolation.app.domain.model.Metrics

object QualityAnalyzer : Analyzer {
    override fun analise(function: Function, points: Coordinates, count: Long): Metrics {
        val determination = Determination.calcDetermination(function, points, count)
        return when (function) {
            is Function.Linear -> Metrics.Linear(
                determination,
                Pearson.calcPearson(points, count)
            )

            else -> Metrics.Common(determination)
        }
    }
}

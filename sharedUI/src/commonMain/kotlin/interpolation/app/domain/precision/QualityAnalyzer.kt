package interpolation.app.domain.precision

import interpolation.app.domain.basic.Analyzer
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function
import interpolation.app.domain.model.Metrics

object QualityAnalyzer : Analyzer {
    override fun analise(function: Function, points: Coordinates): Metrics {
        val determination = Determination.calcDetermination(function, points)
        return when (function) {
            is Function.Linear -> Metrics.Linear(
                determination,
                Pearson.calcPearson(points)
            )

            else -> Metrics.Common(determination)
        }
    }
}

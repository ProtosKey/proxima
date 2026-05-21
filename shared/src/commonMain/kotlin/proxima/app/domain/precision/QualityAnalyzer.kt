package proxima.app.domain.precision

import proxima.app.domain.basic.Analyzer
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function
import proxima.app.domain.model.Metrics

object QualityAnalyzer : Analyzer {
    override fun analise(function: Function, points: Coordinates, count: Long): Metrics {
        val det = Determination.calcDetermination(function, points, count)
        return when (function) {
            is Function.Linear -> Metrics.Linear(
                determination = det.r2,
                linear = Pearson.calcPearson(points, count),
                sko = det.sko
            )
            else -> Metrics.Common(
                determination = det.r2,
                sko = det.sko
            )
        }
    }
}

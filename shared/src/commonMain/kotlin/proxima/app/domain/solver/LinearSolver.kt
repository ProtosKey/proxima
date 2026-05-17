package proxima.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.basic.CanSolve
import proxima.app.domain.math.InterpolationEngine
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function

object LinearSolver : CanSolve {
    override fun solve(points: Coordinates, count: Long): Function {
        val lat = mutableListOf<BigDecimal>()
        val lon = mutableListOf<BigDecimal>()

        points.forEach { point ->
            lat.add(point.x)
            lon.add(point.y)
        }

        val result = InterpolationEngine.calculate(lat, lon, Function.Linear.size, count)
        return Function.Linear(result[1], result[0])
    }
}

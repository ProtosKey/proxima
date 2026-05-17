package interpolation.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.basic.CanSolve
import interpolation.app.domain.math.InterpolationEngine
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function

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

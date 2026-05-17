package proxima.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.basic.CanSolve
import proxima.app.domain.exception.SolverException
import proxima.app.domain.math.DecimalUtils
import proxima.app.domain.math.InterpolationEngine
import proxima.app.domain.math.ln
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function

object LogarithmSolver : CanSolve {
    override fun solve(points: Coordinates, count: Long): Function {
        val lat = mutableListOf<BigDecimal>()
        val lon = mutableListOf<BigDecimal>()

        points.forEach { point ->
            if (point.x <= 0) {
                throw SolverException("Аргумент должен быть положительным")
            }

            lat.add(point.x.ln(DecimalUtils.getMode(count)))
            lon.add(point.y)
        }

        val result = InterpolationEngine.calculate(lat, lon, Function.Logarithm.size, count)
        return Function.Logarithm(result[1], result[0])
    }
}

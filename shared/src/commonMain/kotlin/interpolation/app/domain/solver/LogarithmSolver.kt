package interpolation.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.basic.CanSolve
import interpolation.app.domain.exception.SolverException
import interpolation.app.domain.math.DecimalUtils
import interpolation.app.domain.math.InterpolationEngine
import interpolation.app.domain.math.ln
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function

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

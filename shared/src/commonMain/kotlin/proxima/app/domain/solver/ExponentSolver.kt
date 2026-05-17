package proxima.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.basic.CanSolve
import proxima.app.domain.exception.SolverException
import proxima.app.domain.math.DecimalUtils
import proxima.app.domain.math.InterpolationEngine
import proxima.app.domain.math.exp
import proxima.app.domain.math.ln
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function

object ExponentSolver : CanSolve {
    override fun solve(points: Coordinates, count: Long): Function {
        val lat = mutableListOf<BigDecimal>()
        val lon = mutableListOf<BigDecimal>()

        points.forEach { point ->
            if (point.y <= 0) {
                throw SolverException("Аргумент должен быть положительным")
            }

            lat.add(point.x)
            lon.add(point.y.ln(DecimalUtils.getMode(count)))
        }

        val result = InterpolationEngine.calculate(lat, lon, Function.Exponent.size, count)
        return Function.Exponent(result[0].exp(DecimalUtils.getMode(count)), result[1])
    }
}

package interpolation.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.basic.CanSolve
import interpolation.app.domain.exception.SolverException
import interpolation.app.domain.math.DecimalUtils
import interpolation.app.domain.math.InterpolationEngine
import interpolation.app.domain.math.exp
import interpolation.app.domain.math.ln
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function

object ExponentSolver : CanSolve {
    override fun solve(points: Coordinates): Function {
        val lat = mutableListOf<BigDecimal>()
        val lon = mutableListOf<BigDecimal>()

        points.forEach { point ->
            if (point.y <= 0) {
                throw SolverException("Аргумент должен быть положительным")
            }

            lat.add(point.x)
            lon.add(point.y.ln(DecimalUtils.DIVIDE_MODE))
        }

        val result = InterpolationEngine.calculate(lat, lon, Function.Exponent.size)
        return Function.Exponent(result[0].exp(DecimalUtils.DIVIDE_MODE), result[1])
    }
}

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

object PowerSolver : CanSolve {
    override fun solve(points: Coordinates, count: Long): Function {
        val lat = mutableListOf<BigDecimal>()
        val lon = mutableListOf<BigDecimal>()

        points.forEach { point ->
            if (point.y <= 0 || point.x <= 0) {
                throw SolverException("Аргумент должен быть положительным")
            }

            lat.add(point.x.ln(DecimalUtils.getMode(count)))
            lon.add(point.y.ln(DecimalUtils.getMode(count)))
        }

        val result = InterpolationEngine.calculate(lat, lon, Function.Power.size, count)
        return Function.Power(result[0].exp(DecimalUtils.getMode(count)), result[1])
    }
}

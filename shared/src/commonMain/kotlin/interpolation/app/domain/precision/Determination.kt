package interpolation.app.domain.precision

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import interpolation.app.domain.exception.PrecisionException
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function
import interpolation.app.domain.math.DecimalUtils

object Determination {
    fun calcDetermination(function: Function, points: Coordinates, count: Long): BigDecimal {
        val middle =
            points.fold(BigDecimal.ZERO) { acc, point -> acc + function.calculate(point.x, count) }.divide(
                points.size.toBigDecimal(),
                DecimalUtils.getMode(count)
            )
        val top =
            points.fold(BigDecimal.ZERO) { acc, point ->
                acc + (point.y - function.calculate(point.x, count)).pow(
                    2
                )
            }
        val down = points.fold(BigDecimal.ZERO) { acc, point -> acc + (point.y - middle).pow(2) }

        return if (down.isZero()) {
            throw PrecisionException("Не удалось посчитать коэффициент детерминации")
        } else {
            BigDecimal.ONE - top.divide(down, DecimalUtils.getMode(count))
        }
    }
}

package proxima.app.domain.precision

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import proxima.app.domain.exception.PrecisionException
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function
import proxima.app.domain.math.DecimalUtils

object Determination {
    fun calcDetermination(function: Function, points: Coordinates, count: Long): BigDecimal {
        val mode = DecimalUtils.getMode(count)
        val calculated = points.map { function.calculate(it.x, count) }

        val middle = calculated.fold(BigDecimal.ZERO) { acc, v -> acc + v }
            .divide(points.size.toBigDecimal(), mode)

        val top = points.foldIndexed(BigDecimal.ZERO) { i, acc, point ->
            acc + (point.y - calculated[i]).pow(2)
        }
        val down = points.fold(BigDecimal.ZERO) { acc, point -> acc + (point.y - middle).pow(2) }

        return if (down.isZero()) {
            throw PrecisionException("Не удалось посчитать коэффициент детерминации")
        } else {
            BigDecimal.ONE - top.divide(down, mode)
        }
    }
}

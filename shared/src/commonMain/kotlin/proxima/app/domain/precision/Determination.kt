package proxima.app.domain.precision

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import proxima.app.domain.exception.PrecisionException
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function
import proxima.app.domain.math.DecimalUtils
import proxima.app.domain.math.sqrt

data class DeterminationResult(
    val r2: BigDecimal,
    val sko: BigDecimal
)

object Determination {
    fun calcDetermination(function: Function, points: Coordinates, count: Long): DeterminationResult {
        val mode = DecimalUtils.getMode(count)
        val calculated = points.map { function.calculate(it.x, count) }

        val middle = calculated.fold(BigDecimal.ZERO) { acc, v -> acc + v }
            .divide(points.size.toBigDecimal(), mode)

        val top = points.foldIndexed(BigDecimal.ZERO) { i, acc, point ->
            acc + (point.y - calculated[i]).pow(2)
        }
        val down = points.fold(BigDecimal.ZERO) { acc, point -> acc + (point.y - middle).pow(2) }

        if (down.isZero()) {
            throw PrecisionException("Не удалось посчитать коэффициент детерминации")
        }

        val r2 = BigDecimal.ONE - top.divide(down, mode)
        val sko = top.divide(points.size.toBigDecimal(), mode).sqrt(mode)

        return DeterminationResult(r2 = r2, sko = sko)
    }
}

package proxima.app.domain.precision

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import proxima.app.domain.exception.PrecisionException
import proxima.app.domain.model.Coordinates
import proxima.app.domain.math.DecimalUtils
import proxima.app.domain.math.sqrt

object Pearson {
    fun calcPearson(points: Coordinates, count: Long): BigDecimal {
        var lat = BigDecimal.ZERO
        var lon = BigDecimal.ZERO
        points.forEach { point -> lat += point.x; lon += point.y }
        lat = lat.divide(points.size.toBigDecimal(), DecimalUtils.getMode(count))
        lon = lon.divide(points.size.toBigDecimal(), DecimalUtils.getMode(count))

        var top = BigDecimal.ZERO
        points.forEach { point -> top += (point.x - lat) * (point.y - lon) }

        var first = BigDecimal.ZERO
        var second = BigDecimal.ZERO
        points.forEach { point ->
            first += (point.x - lat).pow(2); second += (point.y - lon).pow(
            2
        )
        }

        return if (first.isZero() || second.isZero()) {
            throw PrecisionException("Не удалось посчитать коэффициент Пирсона")
        } else {
            top.divide(
                (first * second).sqrt(DecimalUtils.getMode(count)),
                DecimalUtils.getMode(count)
            )
        }
    }
}

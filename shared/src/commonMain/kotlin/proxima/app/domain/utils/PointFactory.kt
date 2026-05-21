package proxima.app.domain.utils

import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import proxima.app.domain.math.DecimalUtils
import proxima.app.domain.model.Point
import kotlin.random.Random

object PointFactory {
    fun random(): Point = Point(randomBigDecimal(), randomBigDecimal())

    private fun randomBigDecimal() =
        Random.nextInt(1100).toBigDecimal()
            .divide("100".toBigDecimal(), DecimalUtils.getMode(2L))
}

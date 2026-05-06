package interpolation.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Point
import interpolation.app.presentation.model.MessageType
import kotlin.random.Random

object Defaults {
    private const val POINT_COUNT = 3

    fun message(): String {
        return "Начните работу..."
    }

    fun messageType(): MessageType {
        return MessageType.GOOD
    }

    fun visible(): Boolean {
        return false
    }

    fun coordinates(): Coordinates {
        val points = MutableList(POINT_COUNT) { randomPoint() }
        return Coordinates(points)
    }

    private fun randomPoint(): Point {
        return Point(randomBigDecimal(), randomBigDecimal())
    }

    private fun randomBigDecimal(): BigDecimal {
        return ((Random.nextDouble() - .5) * 20).toBigDecimal()
    }
}

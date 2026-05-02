package interpolation.app.domain.math

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

fun BigDecimal.toDouble(): Double {
    return this.toString().toDouble()
}

fun BigDecimal.sqrt(decimalMode: DecimalMode): BigDecimal {
    if (this.isZero()) {
        return BigDecimal.ZERO
    }
    if (this.signum() == -1) {
        throw ArithmeticException("Нельзя взять корень из отрицательного числа")
    }

    val two = BigDecimal.fromInt(2)
    var x = this.divide(two, decimalMode)
    repeat(DecimalUtils.SQRT_REPEATS) {
        val next = x.add(this.divide(x, decimalMode)).divide(two, decimalMode)
        x = next
    }
    return x
}

fun BigDecimal.ln(decimalMode: DecimalMode): BigDecimal {
    if (this <= BigDecimal.ZERO) {
        throw ArithmeticException("Логарифм только для положительных чисел")
    }
    val pre = this - BigDecimal.ONE
    val aft = this + BigDecimal.ONE
    var rem = pre.divide(aft, decimalMode)
    val dob = rem.pow(2)
    var result = BigDecimal.ZERO

    for (n in 0 until DecimalUtils.LN_REPEATS) {
        val div = 2 * n + 1
        result += rem.divide(div.toBigDecimal(), decimalMode)
        rem *= dob
    }
    return result * 2
}

object DecimalUtils {
    val DIVIDE_MODE =
        DecimalMode(decimalPrecision = 80, roundingMode = RoundingMode.ROUND_HALF_FLOOR)
    const val SQRT_REPEATS = 10
    const val LN_REPEATS = 100
}

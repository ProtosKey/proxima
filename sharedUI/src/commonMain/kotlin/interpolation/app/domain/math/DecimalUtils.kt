package interpolation.app.domain.math

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

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

fun BigDecimal.exp(decimalMode: DecimalMode): BigDecimal {
    if (this.isZero()) {
        return BigDecimal.ONE
    } else if (this.signum() == -1) {
        return BigDecimal.ONE.divide(this.negate().exp(decimalMode), decimalMode)
    }

    var rem = BigDecimal.ONE
    var res = rem
    for (n in 1 until DecimalUtils.EXP_REPEATS) {
        rem = (rem * this).divide(n.toBigDecimal(), decimalMode)
        if (rem.isZero()) {
            break
        }
        res += rem
    }
    return res
}

fun BigDecimal.pow(add: BigDecimal, decimalMode: DecimalMode): BigDecimal {
    if (add.compareTo(BigDecimal.ONE) == 0) {
        return this
    } else if (add.isZero()) {
        return BigDecimal.ONE
    } else if (this.isZero()) {
        return BigDecimal.ZERO
    } else if (this.compareTo(BigDecimal.ONE) == 0) {
        return BigDecimal.ONE
    }
    return (add * this.ln(decimalMode)).exp(decimalMode)
}

object DecimalUtils {
    val DIVIDE_MODE =
        DecimalMode(decimalPrecision = 80, roundingMode = RoundingMode.ROUND_HALF_FLOOR)
    const val SQRT_REPEATS = 10
    const val LN_REPEATS = 100
    const val EXP_REPEATS = 200
}

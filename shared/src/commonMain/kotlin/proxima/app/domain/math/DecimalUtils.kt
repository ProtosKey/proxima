package proxima.app.domain.math

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

fun BigDecimal.toFloat(): Float = this.toString().toFloat()
fun BigDecimal.toDouble(): Double = this.toString().toDouble()

private fun getEpsilon(decimalMode: DecimalMode): BigDecimal {
    return BigDecimal.ONE.divide(
        BigDecimal.fromInt(10).pow(decimalMode.decimalPrecision),
        decimalMode
    )
}

fun BigDecimal.sqrt(decimalMode: DecimalMode): BigDecimal {
    if (this.isZero()) {
        return BigDecimal.ZERO
    }
    if (this.signum() == -1) {
        throw ArithmeticException("Нельзя взять корень из отрицательного числа")
    }

    val epsilon = getEpsilon(decimalMode)
    val two = BigDecimal.fromInt(2)
    var x = this.divide(two, decimalMode)

    while (true) {
        val next = x.add(this.divide(x, decimalMode)).divide(two, decimalMode)
        if ((next - x).abs() <= epsilon)
            break
        x = next
    }
    return x
}

fun BigDecimal.ln(decimalMode: DecimalMode): BigDecimal {
    if (this <= BigDecimal.ZERO) {
        throw ArithmeticException("Логарифм только для положительных чисел")
    }

    val epsilon = getEpsilon(decimalMode)
    val pre = this - BigDecimal.ONE
    val aft = this + BigDecimal.ONE
    var rem = pre.divide(aft, decimalMode)
    val dob = rem.pow(2)
    var result = BigDecimal.ZERO
    var n = 0

    while (true) {
        val div = 2 * (n++) + 1
        val del = rem.divide(div.toBigDecimal(), decimalMode)
        result += del
        if (del.abs() <= epsilon && n > 0 || n > 1000)
            break
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

    val epsilon = getEpsilon(decimalMode)
    var res = BigDecimal.ONE
    var rem = BigDecimal.ONE
    var n = 1

    while (true) {
        rem = (rem * this).divide((n++).toBigDecimal(), decimalMode)
        res += rem
        if (rem.abs() <= epsilon && n != 0 || n > 1000)
            break
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
    fun getMode(precision: Long): DecimalMode {
        return DecimalMode(
            decimalPrecision = precision,
            roundingMode = RoundingMode.ROUND_HALF_FLOOR
        )
    }
}

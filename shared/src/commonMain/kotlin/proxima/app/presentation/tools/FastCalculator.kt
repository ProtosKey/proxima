package proxima.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.basic.FunctionVisitor
import proxima.app.domain.math.toDouble
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow

object FastCalculator : FunctionVisitor<FastFunction> {
    override fun visitLinear(
        a: BigDecimal,
        b: BigDecimal,
    ): FastFunction {
        val aD = a.toDouble()
        val bD = b.toDouble()
        return { x -> aD * x + bD }
    }

    override fun visitSecond(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
    ): FastFunction {
        val aD = a.toDouble()
        val bD = b.toDouble()
        val cD = c.toDouble()
        return { x -> aD * x.pow(2) + bD * x + cD }
    }

    override fun visitThird(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        d: BigDecimal,
    ): FastFunction {
        val aD = a.toDouble()
        val bD = b.toDouble()
        val cD = c.toDouble()
        val dD = d.toDouble()
        return { x -> aD * x.pow(3) + bD * x.pow(2) + cD * x + dD }
    }

    override fun visitExponent(
        a: BigDecimal,
        b: BigDecimal,
    ): FastFunction {
        val aD = a.toDouble()
        val bD = b.toDouble()
        return { x -> aD * exp(bD * x) }
    }

    override fun visitLogarithm(
        a: BigDecimal,
        b: BigDecimal,
    ): FastFunction {
        val aD = a.toDouble()
        val bD = b.toDouble()
        return { x ->
            if (x <= 0.0)
                throw ArithmeticException("x должен быть положительным")
            aD * ln(x) + bD
        }
    }

    override fun visitPower(
        a: BigDecimal,
        b: BigDecimal,
    ): FastFunction {
        val aD = a.toDouble()
        val bD = b.toDouble()
        return { x ->
            if (x < 0.0)
                throw ArithmeticException("x должен быть неотрицательным")
            aD * x.pow(bD)
        }
    }
}

fun interface FastFunction {
    fun calculate(value: Double): Double
}

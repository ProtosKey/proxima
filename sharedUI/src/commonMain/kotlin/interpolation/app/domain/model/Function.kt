package interpolation.app.domain.model;

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import interpolation.app.domain.basic.CanVisit
import interpolation.app.domain.basic.FunctionInfo
import interpolation.app.domain.basic.FunctionVisitor
import interpolation.app.domain.exception.FunctionException
import interpolation.app.domain.math.DecimalUtils
import interpolation.app.domain.math.ln
import interpolation.app.domain.math.toDouble
import kotlin.math.exp
import kotlin.math.pow

sealed class Function : CanVisit {
    abstract fun calculate(value: BigDecimal): BigDecimal

    class Linear(
        private val a: BigDecimal,
        private val b: BigDecimal
    ) : Function() {
        companion object : FunctionInfo {
            override val size = 2
        }

        override fun calculate(value: BigDecimal): BigDecimal {
            return value * a + b
        }

        override fun acceptVisitor(visitor: FunctionVisitor): String {
            return visitor.visitLinear(a, b)
        }
    }

    class Second(
        private val a: BigDecimal,
        private val b: BigDecimal,
        private val c: BigDecimal
    ) : Function() {
        companion object : FunctionInfo {
            override val size = 3
        }

        override fun calculate(value: BigDecimal): BigDecimal {
            return a * value.pow(2) + b * value + c
        }

        override fun acceptVisitor(visitor: FunctionVisitor): String {
            return visitor.visitSecond(a, b, c)
        }
    }

    class Third(
        private val a: BigDecimal,
        private val b: BigDecimal,
        private val c: BigDecimal,
        private val d: BigDecimal
    ) : Function() {
        companion object : FunctionInfo {
            override val size = 4
        }

        override fun calculate(value: BigDecimal): BigDecimal {
            return a * value.pow(3) + b * value.pow(2) + c * value + d
        }

        override fun acceptVisitor(visitor: FunctionVisitor): String {
            return visitor.visitThird(a, b, c, d)
        }
    }

    class Exponent(
        private val a: BigDecimal,
        private val b: BigDecimal
    ) : Function() {
        companion object : FunctionInfo {
            override val size = 2
        }

        override fun calculate(value: BigDecimal): BigDecimal {
            return a * exp((b * value).toDouble()).toBigDecimal()
        }

        override fun acceptVisitor(visitor: FunctionVisitor): String {
            return visitor.visitExponent(a, b)
        }
    }

    class Logarithm(
        private val a: BigDecimal,
        private val b: BigDecimal
    ) : Function() {
        companion object : FunctionInfo {
            override val size = 2
        }

        override fun calculate(value: BigDecimal): BigDecimal {
            if (value <= BigDecimal.ZERO) {
                throw FunctionException("Аргумент логарифма должен быть больше нуля")
            }
            return a * value.ln(DecimalUtils.DIVIDE_MODE) + b
        }

        override fun acceptVisitor(visitor: FunctionVisitor): String {
            return visitor.visitLogarithm(a, b)
        }
    }

    class Power(
        private val a: BigDecimal,
        private val b: BigDecimal
    ) : Function() {
        companion object : FunctionInfo {
            override val size = 2
        }

        override fun calculate(value: BigDecimal): BigDecimal {
            return a * value.toDouble().pow(b.toDouble()).toBigDecimal()
        }

        override fun acceptVisitor(visitor: FunctionVisitor): String {
            return visitor.visitPower(a, b)
        }
    }
}

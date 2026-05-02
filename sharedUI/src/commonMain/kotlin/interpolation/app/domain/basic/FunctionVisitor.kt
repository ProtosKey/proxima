package interpolation.app.domain.basic

import com.ionspin.kotlin.bignum.decimal.BigDecimal

interface FunctionVisitor {
    fun visitLinear(a: BigDecimal, b: BigDecimal): String
    fun visitSecond(a: BigDecimal, b: BigDecimal, c: BigDecimal): String
    fun visitThird(a: BigDecimal, b: BigDecimal, c: BigDecimal, d: BigDecimal): String
    fun visitExponent(a: BigDecimal, b: BigDecimal): String
    fun visitLogarithm(a: BigDecimal, b: BigDecimal): String
    fun visitPower(a: BigDecimal, b: BigDecimal): String
}

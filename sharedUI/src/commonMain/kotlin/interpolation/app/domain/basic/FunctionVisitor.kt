package interpolation.app.domain.basic

import com.ionspin.kotlin.bignum.decimal.BigDecimal

interface FunctionVisitor<R> {
    fun visitLinear(
        a: BigDecimal,
        b: BigDecimal,
    ): R

    fun visitSecond(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
    ): R

    fun visitThird(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        d: BigDecimal,
    ): R

    fun visitExponent(
        a: BigDecimal,
        b: BigDecimal,
    ): R

    fun visitLogarithm(
        a: BigDecimal,
        b: BigDecimal,
    ): R

    fun visitPower(
        a: BigDecimal,
        b: BigDecimal,
    ): R
}

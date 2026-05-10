package interpolation.app.domain.basic

import com.ionspin.kotlin.bignum.decimal.BigDecimal

interface FunctionVisitor {
    fun visitLinear(
        a: BigDecimal,
        b: BigDecimal,
        sign: Int
    ): String

    fun visitSecond(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        sign: Int
    ): String

    fun visitThird(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        d: BigDecimal,
        sign: Int
    ): String

    fun visitExponent(
        a: BigDecimal,
        b: BigDecimal,
        sign: Int
    ): String

    fun visitLogarithm(
        a: BigDecimal,
        b: BigDecimal,
        sign: Int
    ): String

    fun visitPower(
        a: BigDecimal,
        b: BigDecimal,
        sign: Int
    ): String
}

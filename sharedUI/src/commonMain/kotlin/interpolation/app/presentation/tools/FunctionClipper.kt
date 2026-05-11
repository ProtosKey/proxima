package interpolation.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.basic.FunctionVisitor

object FunctionClipper : FunctionVisitor<String> {
    private fun getSign(number: BigDecimal): String {
        return if (number >= BigDecimal.ZERO) "+" else "-"
    }

    override fun visitLinear(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "${StringParser.prepareToString(a)} * x ${getSign(b)} " +
                StringParser.prepareToString(b.abs())
    }

    override fun visitSecond(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
    ): String {
        return "${StringParser.prepareToString(a)} * x ** 2 ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs())} * x ${getSign(c)} " +
                StringParser.prepareToString(c.abs())
    }

    override fun visitThird(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        d: BigDecimal,
    ): String {
        return "${StringParser.prepareToString(a)} * x ** 3 ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs())} * x ** 2 " +
                "${getSign(c)} ${StringParser.prepareToString(c.abs())} * x " +
                "${getSign(d)} ${StringParser.prepareToString(d.abs())}"
    }

    override fun visitExponent(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "${StringParser.prepareToString(a)} " +
                "* e ** (${StringParser.prepareToString(b)} * x)"
    }

    override fun visitLogarithm(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "${StringParser.prepareToString(a)} " +
                "* ln(x) ${getSign(b)} " +
                StringParser.prepareToString(b.abs())
    }

    override fun visitPower(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "${StringParser.prepareToString(a)} " +
                "* x ** (${StringParser.prepareToString(b)})"
    }
}

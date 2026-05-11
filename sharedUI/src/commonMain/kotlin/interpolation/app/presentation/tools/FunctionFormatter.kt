package interpolation.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.basic.FunctionVisitor

object FunctionFormatter : FunctionVisitor<String> {
    private const val SIZE = 5

    private fun getSign(number: BigDecimal): String {
        return if (number >= BigDecimal.ZERO) "+" else "-"
    }

    override fun visitLinear(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "f(x) = ${StringParser.prepareToString(a,SIZE)}x ${getSign(b)} " +
                StringParser.prepareToString(b.abs(), SIZE)
    }

    override fun visitSecond(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
    ): String {
        return "s(x) = ${StringParser.prepareToString(a, SIZE)}x^{2} ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs(), SIZE)}x ${getSign(c)} " +
                StringParser.prepareToString(c.abs(), SIZE)
    }

    override fun visitThird(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        d: BigDecimal,
    ): String {
        return "t(x) = ${StringParser.prepareToString(a, SIZE)}x^{3} ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs(), SIZE)}x^{2} " +
                "${getSign(c)} ${StringParser.prepareToString(c.abs(), SIZE)}x " +
                "${getSign(d)} ${StringParser.prepareToString(d.abs(), SIZE)}"
    }

    override fun visitExponent(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "e(x) = ${StringParser.prepareToString(a, SIZE)} " +
                "\\cdot e^{${StringParser.prepareToString(b, SIZE)}x}"
    }

    override fun visitLogarithm(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "l(x) = ${StringParser.prepareToString(a, SIZE)} " +
                "\\cdot \\ln(x) ${getSign(b)} " +
                StringParser.prepareToString(b.abs(), SIZE)
    }

    override fun visitPower(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "p(x) = ${StringParser.prepareToString(a, SIZE)} " +
                "\\cdot x^{${StringParser.prepareToString(b, SIZE)}}"
    }
}

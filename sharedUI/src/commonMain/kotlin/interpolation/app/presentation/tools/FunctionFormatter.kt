package interpolation.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.basic.FunctionVisitor

object FunctionFormatter : FunctionVisitor {
    private fun getSign(number: BigDecimal): String {
        return if (number >= BigDecimal.Companion.ZERO) "+" else "-"
    }

    override fun visitLinear(a: BigDecimal, b: BigDecimal): String {
        return "f(x) = ${StringParser.prepareToString(a)}x ${getSign(b)} " +
                StringParser.prepareToString(b.abs())
    }

    override fun visitSecond(a: BigDecimal, b: BigDecimal, c: BigDecimal): String {
        return "s(x) = ${StringParser.prepareToString(a)}x^{2} ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs())}x ${getSign(c)} " +
                StringParser.prepareToString(c.abs())
    }

    override fun visitThird(a: BigDecimal, b: BigDecimal, c: BigDecimal, d: BigDecimal): String {
        return "t(x) = ${StringParser.prepareToString(a)}x^{3} ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs())}x^{2} " +
                "${getSign(c)} ${StringParser.prepareToString(c.abs())}x " +
                "${getSign(d)} ${StringParser.prepareToString(d.abs())}"
    }

    override fun visitExponent(a: BigDecimal, b: BigDecimal): String {
        return "e(x) = ${StringParser.prepareToString(a)} \\cdot e^{${StringParser.prepareToString(b)}x}"
    }

    override fun visitLogarithm(a: BigDecimal, b: BigDecimal): String {
        return "l(x) = ${StringParser.prepareToString(a)} \\cdot \\ln(x) ${getSign(b)} ${b.abs()}"
    }

    override fun visitPower(a: BigDecimal, b: BigDecimal): String {
        return "p(x) = ${StringParser.prepareToString(a)} \\cdot x^{${StringParser.prepareToString(b)}}"
    }
}

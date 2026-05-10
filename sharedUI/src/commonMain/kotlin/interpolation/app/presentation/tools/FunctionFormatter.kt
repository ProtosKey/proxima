package interpolation.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.basic.FunctionVisitor

object FunctionFormatter : FunctionVisitor {
    private fun getSign(number: BigDecimal): String {
        return if (number >= BigDecimal.ZERO) "+" else "-"
    }

    override fun visitLinear(
        a: BigDecimal,
        b: BigDecimal,
        sign: Int
    ): String {
        return "f(x) = ${StringParser.prepareToString(a, sign)}x ${getSign(b)} " +
                StringParser.prepareToString(b.abs(), sign)
    }

    override fun visitSecond(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        sign: Int
    ): String {
        return "s(x) = ${StringParser.prepareToString(a, sign)}x^{2} ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs(), sign)}x ${getSign(c)} " +
                StringParser.prepareToString(c.abs(), sign)
    }

    override fun visitThird(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        d: BigDecimal,
        sign: Int
    ): String {
        return "t(x) = ${StringParser.prepareToString(a, sign)}x^{3} ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs(), sign)}x^{2} " +
                "${getSign(c)} ${StringParser.prepareToString(c.abs(), sign)}x " +
                "${getSign(d)} ${StringParser.prepareToString(d.abs(), sign)}"
    }

    override fun visitExponent(
        a: BigDecimal,
        b: BigDecimal,
        sign: Int
    ): String {
        return "e(x) = ${StringParser.prepareToString(a, sign)} " +
                "\\cdot e^{${StringParser.prepareToString(b, sign)}x}"
    }

    override fun visitLogarithm(
        a: BigDecimal,
        b: BigDecimal,
        sign: Int
    ): String {
        return "l(x) = ${StringParser.prepareToString(a, sign)} " +
                "\\cdot \\ln(x) ${getSign(b)} " +
                StringParser.prepareToString(b.abs(), sign)
    }

    override fun visitPower(
        a: BigDecimal,
        b: BigDecimal,
        sign: Int
    ): String {
        return "p(x) = ${StringParser.prepareToString(a, sign)} " +
                "\\cdot x^{${StringParser.prepareToString(b, sign)}}"
    }
}

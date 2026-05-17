package proxima.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.basic.FunctionVisitor

class FunctionFormatter(private val count: Int) : FunctionVisitor<String> {
    private fun getSign(number: BigDecimal): String {
        return if (number >= BigDecimal.ZERO) "+" else "-"
    }

    override fun visitLinear(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "f(x) = ${StringParser.prepareToString(a,count)}x ${getSign(b)} " +
                StringParser.prepareToString(b.abs(), count)
    }

    override fun visitSecond(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
    ): String {
        return "s(x) = ${StringParser.prepareToString(a, count)}x^{2} ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs(), count)}x ${getSign(c)} " +
                StringParser.prepareToString(c.abs(), count)
    }

    override fun visitThird(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        d: BigDecimal,
    ): String {
        return "t(x) = ${StringParser.prepareToString(a, count)}x^{3} ${getSign(b)} " +
                "${StringParser.prepareToString(b.abs(), count)}x^{2} " +
                "${getSign(c)} ${StringParser.prepareToString(c.abs(), count)}x " +
                "${getSign(d)} ${StringParser.prepareToString(d.abs(), count)}"
    }

    override fun visitExponent(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "e(x) = ${StringParser.prepareToString(a, count)} " +
                "\\cdot e^{${StringParser.prepareToString(b, count)}x}"
    }

    override fun visitLogarithm(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "l(x) = ${StringParser.prepareToString(a, count)} " +
                "\\cdot \\ln(x) ${getSign(b)} " +
                StringParser.prepareToString(b.abs(), count)
    }

    override fun visitPower(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "p(x) = ${StringParser.prepareToString(a, count)} " +
                "\\cdot x^{${StringParser.prepareToString(b, count)}}"
    }
}

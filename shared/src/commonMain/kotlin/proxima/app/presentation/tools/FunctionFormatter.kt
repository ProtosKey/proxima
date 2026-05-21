package proxima.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.basic.FunctionVisitor

class FunctionFormatter(private val count: Int) : FunctionVisitor<String> {
    private fun format(number: BigDecimal) =
        "\\textrm{${StringParser.prepareToString(number, count)}}"

    private fun getSign(number: BigDecimal): String {
        return if (number >= BigDecimal.ZERO) "+" else "-"
    }

    override fun visitLinear(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "f(x) = ${format(a)}x ${getSign(b)} ${format(b.abs())}"
    }

    override fun visitSecond(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
    ): String {
        return "s(x) = ${format(a)}x^{2} ${getSign(b)} " +
                "${format(b.abs())}x ${getSign(c)} " +
                format(c.abs())
    }

    override fun visitThird(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
        d: BigDecimal,
    ): String {
        return "t(x) = ${format(a)}x^{3} ${getSign(b)} " +
                "${format(b.abs())}x^{2} " +
                "${getSign(c)} ${format(c.abs())}x " +
                "${getSign(d)} ${format(d.abs())}"
    }

    override fun visitExponent(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "e(x) = ${format(a)} " +
                "\\cdot e^{${format(b)}x}"
    }

    override fun visitLogarithm(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "l(x) = ${format(a)} " +
                "\\cdot \\ln(x) ${getSign(b)} " +
                format(b.abs())
    }

    override fun visitPower(
        a: BigDecimal,
        b: BigDecimal,
    ): String {
        return "p(x) = ${format(a)} " +
                "\\cdot x^{${format(b)}}"
    }
}

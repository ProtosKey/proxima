package interpolation.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import interpolation.app.presentation.exception.ParserException

object StringParser {
    private val ZEROS = "0*$".toRegex()
    private val EXTRA = "\\.$".toRegex()
    private val ZERO_EPSILON = "1E-79".toBigDecimal()
    private const val DEFAULT_BODY = "Начните работу..."

    fun prepareToString(number: BigDecimal): String {
        return prepareNumber(number.toPlainString())
    }

    fun prepareNumber(value: String): String {
        var result = value.replace(",", ".").trim()
        if (result.isEmpty()) return ""

        result = when {
            result.startsWith(".") -> "0$result"
            result.startsWith("-.") -> result.replace("-.", "-0.")
            else -> result
        }

        return removeZeros(
            if (result.startsWith("-")) {
                "-" + result.substring(1).replaceFirst("^0+(?=\\d)".toRegex(), "")
            } else {
                result.replaceFirst("^0+(?=\\d)".toRegex(), "")
            }
        )
    }

    fun parseBigDecimal(value: String): BigDecimal {
        try {
            return prepareNumber(value).toBigDecimal()
        } catch (_: NumberFormatException) {
            if (value.isEmpty()) {
                throw ParserException("Значение не может быть пустым")
            } else {
                throw ParserException("Значение <$value> должно быть числом")
            }
        }
    }

    fun removeZeros(value: String): String {
        if (value.isEmpty() || !value.contains(".")) return value
        return value
            .replace(ZEROS, "")
            .replace(EXTRA, "")
    }

    fun checkZero(value: BigDecimal): BigDecimal {
        return if ((value).abs() < ZERO_EPSILON) BigDecimal.ZERO else value
    }

    fun prepareFunction(body: String): String {
        return body.ifEmpty {
            DEFAULT_BODY
        }
    }
}

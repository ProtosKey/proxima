package proxima.app.presentation.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import proxima.app.presentation.exception.ParserException
import kotlin.math.min

object StringParser {
    private val ZEROS = "0*$".toRegex()
    private val EXTRA = "\\.$".toRegex()

    fun prepareToString(number: BigDecimal, sign: Int = -1): String {
        val result = prepareNumber(number.toPlainString())
        return if (sign < 0)
            result
        else
            shortResult(result, sign)
    }

    fun shortResult(result: String, sign: Int): String {
        return result.split(".").mapIndexedNotNull { index, string ->
            if (index != 0)
                if (sign == 0)
                    null
                else
                    string.substring(0, min(string.length, sign))
            else
                string
        }.joinToString(".")
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
}

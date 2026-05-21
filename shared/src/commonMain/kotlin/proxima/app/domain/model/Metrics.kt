package proxima.app.domain.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

sealed class Metrics(
    val determination: BigDecimal,
    val sko: BigDecimal
) {
    class Linear(
        determination: BigDecimal,
        val linear: BigDecimal,
        sko: BigDecimal
    ) : Metrics(determination, sko)

    class Common(
        determination: BigDecimal,
        sko: BigDecimal
    ) : Metrics(determination, sko)
}

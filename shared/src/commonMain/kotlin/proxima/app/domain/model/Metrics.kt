package proxima.app.domain.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

sealed class Metrics(val determination: BigDecimal) {
    class Linear(determination: BigDecimal, val linear: BigDecimal) : Metrics(determination)
    class Common(determination: BigDecimal) : Metrics(determination)
}

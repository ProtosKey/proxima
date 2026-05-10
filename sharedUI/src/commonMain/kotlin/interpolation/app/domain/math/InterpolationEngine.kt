package interpolation.app.domain.math

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.exception.EngineException
import interpolation.app.domain.model.Coordinates.Companion.MAX_SIZE
import interpolation.app.domain.model.Coordinates.Companion.MIN_SIZE

internal object InterpolationEngine {
    fun calculate(x: List<BigDecimal>, y: List<BigDecimal>, size: Int): List<BigDecimal> {
        if (x.size != y.size) {
            throw EngineException("Неверное количество точек")
        } else if (x.size !in MIN_SIZE..MAX_SIZE) {
            throw EngineException("Количество точек должно быть от $MIN_SIZE до $MAX_SIZE")
        } else if (size <= 0) {
            throw EngineException("Отрицательное количество параметров")
        }

        val params = MutableList(size) { MutableList(size) { BigDecimal.ZERO } }
        val vector = MutableList(size) { BigDecimal.ZERO }

        for (i in 0 until size) {
            params[i] = (0 until size).map { add ->
                x.indices.fold(BigDecimal.ZERO) { acc, j ->
                    acc + x[j].pow(
                        i + add
                    )
                }
            }.toMutableList()
            vector[i] = y.indices.fold(BigDecimal.ZERO) { acc, j ->
                acc + x[j].pow(
                    i
                ) * y[j]
            }
        }

        return GaussSolver.solveSystem(params, vector)
    }
}

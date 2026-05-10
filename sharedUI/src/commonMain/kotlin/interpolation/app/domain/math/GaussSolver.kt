package interpolation.app.domain.math

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import interpolation.app.domain.exception.SolverException

object GaussSolver {
    fun solveSystem(
        params: MutableList<MutableList<BigDecimal>>,
        vector: MutableList<BigDecimal>
    ): List<BigDecimal> {
        if (params.any { list -> list.size != params.size } || params.size != vector.size) {
            throw SolverException("Неверная система линейных уравнений")
        }

        val size = params.size
        for (i in 0 until size) {
            var found = true
            if (params[i][i].isZero()) {
                found = false
                for (k in i until size) {
                    if (!params[k][i].isZero()) {
                        if (k != i) {
                            swap(params, vector, i, k)
                        }
                        found = true
                        break
                    }
                }
            }

            if (!found) {
                throw SolverException("Матрица вырождена, система не имеет единственного решения")
            }

            for (k in i + 1 until size) {
                val c = params[k][i].divide(
                    params[i][i], DecimalUtils.DIVIDE_MODE
                )
                addMultiplyRow(params, vector, c, i, k);
            }
        }

        val solution = mutableListOf<BigDecimal>()
        for (i in size - 1 downTo 0) {
            var s = BigDecimal.ZERO
            for (j in i + 1 until size) {
                s += params[i][j] * solution[size - 1 - j]
            }

            val a = params[i][i]
            solution.add((vector[i] - s).divide(a, DecimalUtils.DIVIDE_MODE))
        }
        return solution.reversed()
    }

    private fun swap(
        params: MutableList<MutableList<BigDecimal>>,
        vector: MutableList<BigDecimal>, from: Int, to: Int
    ) {
        run {
            val tmp = params[from]
            params[from] = params[to]
            params[to] = tmp
        }
        run {
            val tmp = vector[from]
            vector[from] = vector[to]
            vector[to] = tmp
        }
    }

    private fun addMultiplyRow(
        params: MutableList<MutableList<BigDecimal>>,
        vector: MutableList<BigDecimal>,
        c: BigDecimal,
        from: Int,
        to: Int,
    ) {
        val size = params.size
        vector[to] = vector[to] - (c * vector[from])

        for (j in from until size) {
            params[to][j] = params[to][j] - (c * params[from][j])
        }
    }
}

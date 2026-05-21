package proxima.app.domain.precision

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Point
import proxima.app.domain.solver.LinearSolver
import proxima.app.domain.solver.SquareSolver
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class DeterminationTest {

    private val precision = 32L

    private fun coords(vararg pairs: Pair<Int, Int>): Coordinates =
        Coordinates(pairs.map { (x, y) ->
            Point(BigDecimal.fromInt(x), BigDecimal.fromInt(y))
        }.toMutableList())

    private fun assertClose(expected: Double, actual: Double, tolerance: Double = 1e-6) =
        assertTrue(abs(expected - actual) < tolerance, "Expected $expected but got $actual")

    @Test
    fun perfectLinearFit_r2IsOne() {
        val points = coords(1 to 3, 2 to 5, 3 to 7)
        val function = LinearSolver.solve(points, precision)
        val result = Determination.calcDetermination(function, points, precision)

        assertClose(1.0, result.r2.doubleValue(false))
    }

    @Test
    fun scatteredPoints_r2IsBelowOne() {
        val points = coords(1 to 1, 2 to 8, 3 to 2)
        val function = LinearSolver.solve(points, precision)
        val result = Determination.calcDetermination(function, points, precision)

        assertTrue(result.r2.doubleValue(false) < 0.9, "R² должен быть < 0.9 для разбросанных точек")
    }

    @Test
    fun perfectQuadraticFit_r2IsOne() {
        val points = coords(1 to 1, 2 to 4, 3 to 9)
        val function = SquareSolver.solve(points, precision)
        val result = Determination.calcDetermination(function, points, precision)

        assertClose(1.0, result.r2.doubleValue(false), tolerance = 1e-4)
    }

    @Test
    fun perfectFit_skoIsZero() {
        val points = coords(1 to 3, 2 to 5, 3 to 7)
        val function = LinearSolver.solve(points, precision)
        val result = Determination.calcDetermination(function, points, precision)

        assertClose(0.0, result.sko.doubleValue(false))
    }

    @Test
    fun scatteredPoints_skoIsPositive() {
        val points = coords(1 to 1, 2 to 8, 3 to 2)
        val function = LinearSolver.solve(points, precision)
        val result = Determination.calcDetermination(function, points, precision)

        assertTrue(result.sko.doubleValue(false) > 0.0, "SKO должен быть > 0 при наличии отклонений")
    }
}

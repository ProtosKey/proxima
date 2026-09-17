package proxima.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Point
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class CubicSolverTest {
    private val precision = 32L

    private fun coords(vararg pairs: Pair<Int, Int>): Coordinates =
        Coordinates(pairs.map { (x, y) ->
            Point(BigDecimal.fromInt(x), BigDecimal.fromInt(y))
        }.toMutableList())

    private fun assertClose(expected: Double, actual: Double, tolerance: Double = 1e-6) =
        assertTrue(abs(expected - actual) < tolerance, "Expected $expected but got $actual")

    private fun calc(function: proxima.app.domain.model.Function, x: Int) =
        function.calculate(BigDecimal.fromInt(x), precision).doubleValue(false)

    @Test
    fun fourDistinctPoints_exactFit() {
        // Четыре точки с разными x однозначно определяют кубическую функцию —
        // подобранная кривая обязана пройти через них точно, независимо от того,
        // "красивые" это значения или нет.
        val points = coords(0 to 1, 1 to 4, 2 to 9, 3 to 2)
        val function = CubicSolver.solve(points, precision)

        assertClose(1.0, calc(function, 0))
        assertClose(4.0, calc(function, 1))
        assertClose(9.0, calc(function, 2))
        assertClose(2.0, calc(function, 3))
    }

    @Test
    fun perfectCube_reproducesKnownCurve() {
        val points = coords(0 to 0, 1 to 1, 2 to 8, 3 to 27)
        val function = CubicSolver.solve(points, precision)

        assertClose(64.0, calc(function, 4))
    }
}

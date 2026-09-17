package proxima.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Point
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class SquareSolverTest {
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
    fun threeDistinctPoints_exactFit() {
        // Три точки с разными x всегда однозначно определяют квадратичную функцию —
        // подобранная кривая обязана пройти через них точно.
        val points = coords(0 to 2, 1 to 3, 2 to 8)
        val function = SquareSolver.solve(points, precision)

        assertClose(2.0, calc(function, 0))
        assertClose(3.0, calc(function, 1))
        assertClose(8.0, calc(function, 2))
    }

    @Test
    fun perfectSquare_reproducesKnownParabola() {
        val points = coords(-1 to 1, 0 to 0, 1 to 1)
        val function = SquareSolver.solve(points, precision)

        assertClose(4.0, calc(function, 2))
        assertClose(9.0, calc(function, 3))
    }
}

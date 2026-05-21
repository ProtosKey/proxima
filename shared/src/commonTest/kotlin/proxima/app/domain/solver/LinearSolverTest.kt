package proxima.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Point
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue
class LinearSolverTest {
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
    fun perfectFit_slopeAndIntercept() {
        val points = coords(1 to 3, 2 to 5, 3 to 7)
        val function = LinearSolver.solve(points, precision)

        assertClose(3.0, calc(function, 1))
        assertClose(5.0, calc(function, 2))
        assertClose(7.0, calc(function, 3))
    }

    @Test
    fun horizontalLine_slopeIsZero() {
        val points = coords(1 to 5, 2 to 5, 3 to 5)
        val function = LinearSolver.solve(points, precision)

        assertClose(5.0, calc(function, 10))
        assertClose(5.0, calc(function, 100))
    }

    @Test
    fun negativeSlope() {
        val points = coords(1 to 3, 2 to 2, 3 to 1)
        val function = LinearSolver.solve(points, precision)

        assertClose(3.0, calc(function, 1))
        assertClose(2.0, calc(function, 2))
        assertClose(1.0, calc(function, 3))
    }
}

package proxima.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.exception.SolverException
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Point
import kotlin.math.abs
import kotlin.math.exp
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ExponentSolverTest {
    private val precision = 32L

    private fun coords(vararg pairs: Pair<Int, Int>): Coordinates =
        Coordinates(pairs.map { (x, y) ->
            Point(BigDecimal.fromInt(x), BigDecimal.fromInt(y))
        }.toMutableList())

    private fun coordsD(vararg pairs: Pair<Double, Double>): Coordinates =
        Coordinates(pairs.map { (x, y) ->
            Point(BigDecimal.parseString(x.toString()), BigDecimal.parseString(y.toString()))
        }.toMutableList())

    private fun assertClose(expected: Double, actual: Double, tolerance: Double = 1e-6) =
        assertTrue(abs(expected - actual) < tolerance, "Expected $expected but got $actual")

    private fun calcD(function: proxima.app.domain.model.Function, x: Double) =
        function.calculate(BigDecimal.parseString(x.toString()), precision).doubleValue(false)

    @Test
    fun threePointsOnKnownCurve_exactFit() {
        // Минимум точек в приложении — 3 (Coordinates.MIN_SIZE), поэтому двух точек
        // для проверки 2-параметрической модели недостаточно. Берём 3 точки, реально
        // лежащие на y = a*e^(b*x) (a=2, b=0.5) — значения считаем тем же exp(),
        // которым будет проверяться результат, а не подбираем вручную.
        val a = 2.0
        val b = 0.5
        val xs = doubleArrayOf(0.0, 1.0, 2.0)
        val points = coordsD(*xs.map { it to (a * exp(b * it)) }.toTypedArray())
        val function = ExponentSolver.solve(points, precision)

        xs.forEach { x -> assertClose(a * exp(b * x), calcD(function, x)) }
    }

    @Test
    fun nonPositiveY_throws() {
        val points = coords(0 to 1, 1 to -3)

        assertFailsWith<SolverException> {
            ExponentSolver.solve(points, precision)
        }
    }
}

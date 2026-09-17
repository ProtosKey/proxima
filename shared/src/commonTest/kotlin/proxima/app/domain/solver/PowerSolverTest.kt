package proxima.app.domain.solver

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import proxima.app.domain.exception.SolverException
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Point
import kotlin.math.abs
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PowerSolverTest {
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
        // лежащие на y = a*x^b (a=2, b=1.5) — значения считаем той же pow(),
        // которой будет проверяться результат, а не подбираем вручную.
        val a = 2.0
        val b = 1.5
        val xs = doubleArrayOf(1.0, 2.0, 4.0)
        val points = coordsD(*xs.map { it to (a * it.pow(b)) }.toTypedArray())
        val function = PowerSolver.solve(points, precision)

        xs.forEach { x -> assertClose(a * x.pow(b), calcD(function, x)) }
    }

    @Test
    fun nonPositiveArgumentOrValue_throws() {
        val points = coords(1 to 2, -2 to 8)

        assertFailsWith<SolverException> {
            PowerSolver.solve(points, precision)
        }
    }
}

package proxima.app.data.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import proxima.app.data.model.FunctionType
import proxima.app.data.model.MessageType
import proxima.app.domain.basic.CanSolve
import proxima.app.domain.math.DecimalUtils
import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Point
import proxima.app.domain.solver.CubicSolver
import proxima.app.domain.solver.ExponentSolver
import proxima.app.domain.solver.LinearSolver
import proxima.app.domain.solver.LogarithmSolver
import proxima.app.domain.solver.PowerSolver
import proxima.app.domain.solver.SquareSolver
import kotlin.random.Random

object Defaults {
    private const val POINT_COUNT = 3

    fun exception(): String = "Неожиданная ошибка"

    fun message(): String = "Начните работу..."

    fun messageType(): MessageType = MessageType.GOOD

    fun visible(): Boolean = false

    fun coordinates(): Coordinates {
        val points = MutableList(POINT_COUNT) { randomPoint() }
        return Coordinates(points)
    }

    fun solvers(): Map<FunctionType, CanSolve> = mapOf(
        FunctionType.THIRD to CubicSolver,
        FunctionType.EXPONENT to ExponentSolver,
        FunctionType.LINEAR to LinearSolver,
        FunctionType.LOGARITHM to LogarithmSolver,
        FunctionType.POWER to PowerSolver,
        FunctionType.SECOND to SquareSolver
    )

    fun randomPoint(): Point {
        return Point(randomBigDecimal(), randomBigDecimal())
    }

    private fun randomBigDecimal(): BigDecimal {
        return Random.nextInt(1100).toBigDecimal()
            .divide("100".toBigDecimal(), DecimalUtils.getMode(2L))
    }
}

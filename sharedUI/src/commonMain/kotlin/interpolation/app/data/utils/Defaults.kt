package interpolation.app.data.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import interpolation.app.data.model.FunctionType
import interpolation.app.data.model.MessageType
import interpolation.app.domain.basic.CanSolve
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Point
import interpolation.app.domain.solver.CubicSolver
import interpolation.app.domain.solver.ExponentSolver
import interpolation.app.domain.solver.LinearSolver
import interpolation.app.domain.solver.LogarithmSolver
import interpolation.app.domain.solver.PowerSolver
import interpolation.app.domain.solver.SquareSolver
import kotlin.random.Random

object Defaults {
    private const val POINT_COUNT = 3

    fun newPoint(): Boolean = true

    fun autoUpdate(): Boolean = false

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
        return ((Random.nextDouble() - .5) * 20).toBigDecimal()
    }
}
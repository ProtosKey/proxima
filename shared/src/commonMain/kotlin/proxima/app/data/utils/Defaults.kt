package proxima.app.data.utils

import proxima.app.data.model.FunctionType
import proxima.app.data.model.MessageType
import proxima.app.domain.basic.CanSolve
import proxima.app.domain.solver.CubicSolver
import proxima.app.domain.solver.ExponentSolver
import proxima.app.domain.solver.LinearSolver
import proxima.app.domain.solver.LogarithmSolver
import proxima.app.domain.solver.PowerSolver
import proxima.app.domain.solver.SquareSolver

object Defaults {
    fun exception(): String = "Неожиданная ошибка"

    fun message(): String = "Начните работу..."

    fun messageType(): MessageType = MessageType.GOOD

    fun visible(): Boolean = false

    fun solvers(): Map<FunctionType, CanSolve> = mapOf(
        FunctionType.THIRD to CubicSolver,
        FunctionType.EXPONENT to ExponentSolver,
        FunctionType.LINEAR to LinearSolver,
        FunctionType.LOGARITHM to LogarithmSolver,
        FunctionType.POWER to PowerSolver,
        FunctionType.SECOND to SquareSolver
    )
}

package interpolation.app.domain.basic

import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Function

interface CanSolve {
    fun solve(points: Coordinates, count: Long): Function
}

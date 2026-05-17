package proxima.app.domain.basic

import proxima.app.domain.model.Coordinates
import proxima.app.domain.model.Function

interface CanSolve {
    fun solve(points: Coordinates, count: Long): Function
}

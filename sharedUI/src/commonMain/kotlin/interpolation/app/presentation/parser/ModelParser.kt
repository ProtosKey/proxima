package interpolation.app.presentation.parser

import interpolation.app.domain.model.Coordinates
import interpolation.app.presentation.model.PointEntry
import interpolation.app.presentation.model.PointLabel

object ModelParser {
    fun parsePointsToNumbers(points: Coordinates): MutableList<PointEntry> {
        return points.map { point -> PointEntry(point.x, point.y) }.toMutableList()
    }

    fun parsePointsToLabel(points: Coordinates): MutableList<PointLabel> {
        return points.map { point ->
            PointLabel(
                StringParser.prepareToString(point.x),
                StringParser.prepareToString(point.y)
            )
        }.toMutableList()
    }
}

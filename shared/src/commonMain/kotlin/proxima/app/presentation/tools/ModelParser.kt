package proxima.app.presentation.tools

import proxima.app.domain.model.Coordinates
import proxima.app.presentation.mapper.EntryMapper
import proxima.app.presentation.model.PointEntry

object ModelParser {
    fun parsePointsToNumbers(points: Coordinates): MutableList<PointEntry> {
        return points.map { point -> EntryMapper.mapTo(point) }.toMutableList()
    }

    fun parsePointsToLabel(points: Coordinates): MutableList<PointEntry> {
        return points.map { point -> EntryMapper.mapTo(point) }.toMutableList()
    }
}

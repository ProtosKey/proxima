package interpolation.app.presentation.tools

import interpolation.app.domain.model.Coordinates
import interpolation.app.presentation.mapper.EntryMapper
import interpolation.app.presentation.model.PointEntry

object ModelParser {
    fun parsePointsToNumbers(points: Coordinates): MutableList<PointEntry> {
        return points.map { point -> EntryMapper.mapTo(point) }.toMutableList()
    }

    fun parsePointsToLabel(points: Coordinates): MutableList<PointEntry> {
        return points.map { point -> EntryMapper.mapTo(point) }.toMutableList()
    }
}

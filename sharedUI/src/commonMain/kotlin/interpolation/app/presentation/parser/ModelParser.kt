package interpolation.app.presentation.parser

import interpolation.app.domain.model.Coordinates
import interpolation.app.presentation.mapper.EntryMapper
import interpolation.app.presentation.mapper.LabelMapper
import interpolation.app.presentation.model.PointEntry
import interpolation.app.presentation.model.PointLabel

object ModelParser {
    fun parsePointsToNumbers(points: Coordinates): MutableList<PointEntry> {
        return points.map { point -> EntryMapper.mapTo(point) }.toMutableList()
    }

    fun parsePointsToLabel(points: Coordinates): MutableList<PointLabel> {
        return points.map { point -> LabelMapper.mapTo(point) }.toMutableList()
    }
}

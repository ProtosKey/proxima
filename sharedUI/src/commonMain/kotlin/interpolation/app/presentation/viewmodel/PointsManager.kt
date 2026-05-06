package interpolation.app.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import interpolation.app.domain.model.Coordinates
import interpolation.app.domain.model.Point
import interpolation.app.presentation.exception.ModelException
import interpolation.app.presentation.exception.ParserException
import interpolation.app.presentation.mapper.EntryMapper
import interpolation.app.presentation.tools.Defaults
import interpolation.app.presentation.tools.ModelParser

class PointsManager {
    val points = mutableStateOf(Defaults.coordinates())
    val entries = ModelParser.parsePointsToNumbers(points.value).toMutableStateList()

    fun parseEntries() {
        val newPoints = mutableListOf<Point>()
        for (entry in entries) {
            try {
                newPoints.add(EntryMapper.mapFrom(entry))
            } catch (e: Exception) {
                val message = when (e) {
                    is ParserException -> e.message
                    else -> Defaults.exception()
                }
            }
        }
        points.value = Coordinates(newPoints)
    }

    fun addPoint(point: Point) {
        entries.add(EntryMapper.mapTo(point))
        points.value.addPoint(point)
        points.value = Coordinates(points.value.points)
    }

    fun deleteByIndex(index: Int) {
        checkIndex(index)
        entries.removeAt(index)
        points.value.removeIndex(index)
        points.value = Coordinates(points.value.points)
    }

    fun updateByIndex(point: Point, index: Int) {
        checkIndex(index)
        entries[index] = EntryMapper.mapTo(point)
        points.value[index] = point
        points.value = Coordinates(points.value.points)
    }

    private fun checkIndex(index: Int) {
        if (index < 0 || index >= entries.size)
            throw ModelException("Точки с таким индексом не существует")
    }
}

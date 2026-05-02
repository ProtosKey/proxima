package interpolation.app.domain.model

import interpolation.app.domain.exception.InitException

class Coordinates(val points: MutableList<Point>) : Iterable<Point> {
    companion object {
        const val MAX_SIZE = 12
        const val MIN_SIZE = 2
    }

    init {
        if (points.size > MAX_SIZE) {
            throw InitException("Превышен лимит в $MAX_SIZE точек")
        }
    }

    operator fun get(index: Int): Point = points[index]
    override fun iterator() = points.iterator()
    val size: Int get() = points.size
    fun isEmpty(): Boolean {
        return points.size == 0
    }

    fun addPoint(point: Point) {
        if (points.size == MAX_SIZE) {
            throw InitException("Нельзя добавить еще больше точек")
        }
        points.add(point)
    }

    fun removePoint(point: Point) {
        val removed = points.remove(point)
        if (!removed) {
            throw InitException("Точка не была найдена в списке")
        }
    }

    fun removeIndex(index: Int) {
        if (index < 0 || index >= points.size) {
            throw InitException("Точки с таким индексом не существует")
        }
        points.removeAt(index)
    }

    fun clear() {
        points.clear()
    }

    fun isMaxSize(): Boolean {
        return points.size == MAX_SIZE
    }
}

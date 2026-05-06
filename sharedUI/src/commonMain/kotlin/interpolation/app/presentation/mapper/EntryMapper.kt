package interpolation.app.presentation.mapper

import interpolation.app.domain.model.Point
import interpolation.app.presentation.basic.Mapper
import interpolation.app.presentation.model.PointEntry

object EntryMapper : Mapper<Point, PointEntry> {
    override fun mapTo(t: Point): PointEntry {
        return PointEntry(t.x, t.y)
    }

    override fun mapFrom(r: PointEntry): Point {
        return Point(r.x, r.y)
    }
}

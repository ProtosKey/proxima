package interpolation.app.presentation.mapper

import interpolation.app.domain.model.Point
import interpolation.app.presentation.basic.Mapper
import interpolation.app.presentation.model.PointEntry
import interpolation.app.presentation.tools.StringParser

object EntryMapper : Mapper<Point, PointEntry> {
    override fun mapTo(t: Point): PointEntry {
        return PointEntry(
            StringParser.prepareToString(t.x),
            StringParser.prepareToString(t.y)
        )
    }

    override fun mapFrom(r: PointEntry): Point {
        return Point(
            StringParser.parseBigDecimal(r.x),
            StringParser.parseBigDecimal(r.y)
        )
    }
}

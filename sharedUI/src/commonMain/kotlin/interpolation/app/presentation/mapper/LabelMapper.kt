package interpolation.app.presentation.mapper

import interpolation.app.domain.model.Point
import interpolation.app.presentation.basic.Mapper
import interpolation.app.presentation.model.PointLabel
import interpolation.app.presentation.tools.StringParser

object LabelMapper : Mapper<Point, PointLabel> {
    override fun mapTo(t: Point): PointLabel {
        return PointLabel(
            StringParser.prepareToString(t.x),
            StringParser.prepareToString(t.y)
        )
    }

    override fun mapFrom(r: PointLabel): Point {
        return Point(
            StringParser.parseBigDecimal(r.x),
            StringParser.parseBigDecimal(r.y)
        )
    }
}

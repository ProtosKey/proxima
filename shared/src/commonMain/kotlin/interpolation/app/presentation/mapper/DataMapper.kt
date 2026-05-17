package interpolation.app.presentation.mapper

import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import interpolation.app.domain.math.toFloat
import interpolation.app.domain.model.Point
import interpolation.app.presentation.basic.Mapper
import interpolation.app.presentation.model.PointData
import kotlin.math.round

object DataMapper : Mapper<Point, PointData> {
    override fun mapTo(t: Point): PointData {
        return PointData(
            t.x.toFloat(),
            t.y.toFloat()
        )
    }

    override fun mapFrom(r: PointData): Point {
        return Point(
            r.x.toBigDecimal(),
            r.y.toBigDecimal()
        )
    }
}

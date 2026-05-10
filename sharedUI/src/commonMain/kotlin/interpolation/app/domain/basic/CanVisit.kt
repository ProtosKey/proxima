package interpolation.app.domain.basic

interface CanVisit {
    fun acceptVisitor(visitor: FunctionVisitor, sign: Int = -1): String
}

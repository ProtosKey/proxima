package interpolation.app.domain.basic

interface CanVisit {
    fun acceptVisitor(visitor: FunctionVisitor): String
}

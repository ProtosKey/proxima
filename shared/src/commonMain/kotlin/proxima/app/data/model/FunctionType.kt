package proxima.app.data.model

enum class FunctionType(val label: String) {
    LINEAR("Линейная аппроксимация"),
    SECOND("Квадратичная аппроксимация"),
    THIRD("Кубическая аппроксимация"),
    EXPONENT("Экспоненциальная аппроксимация"),
    LOGARITHM("Логарифмическая аппроксимация"),
    POWER("Показательная аппроксимация")
}

package interpolation.app.data.model

import interpolation.app.data.utils.Defaults

data class Settings(
    val isNewPoints: Setting = Setting(
        parameter = Defaults.newPoint(),
        label = "Переполнение точек",
        Pair(
            "При переполнении точек самая старая точка будет удалена",
            "При переполнении точек будет выброшена ошибка"
        )
    ),
    val isAutoUpdate: Setting = Setting(
        parameter = Defaults.autoUpdate(),
        label = "Автообновление результата",
        description = Pair(
            "Результат обновляется при изменении точек",
            "Результат обновляется только по кнопке"
        ),
    )
)

data class Setting(
    val parameter: Boolean,
    val label: String,
    val description: Pair<String, String>
) {
    fun current(): String {
        return if (parameter)
            description.first
        else
            description.second
    }
}

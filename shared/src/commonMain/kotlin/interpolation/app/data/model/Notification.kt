package interpolation.app.data.model

import interpolation.app.data.utils.Defaults

data class Notification(
    val message: String = Defaults.message(),
    val messageType: MessageType = Defaults.messageType(),
    val isVisible: Boolean = Defaults.visible()
)

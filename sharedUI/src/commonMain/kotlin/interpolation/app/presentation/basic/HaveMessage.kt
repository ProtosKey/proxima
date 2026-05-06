package interpolation.app.presentation.basic

import interpolation.app.data.model.MessageType

interface HaveMessage {
    fun hideMessage()
    fun showMessage(message: String, messageType: MessageType)
}

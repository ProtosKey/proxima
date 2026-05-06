package interpolation.app.presentation.basic

import interpolation.app.data.model.MessageType

interface HaveMessage {
    fun close()
    fun open(message: String, messageType: MessageType)
}

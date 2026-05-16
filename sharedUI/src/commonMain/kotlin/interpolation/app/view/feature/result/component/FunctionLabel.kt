package interpolation.app.view.feature.result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import interpolation.app.data.model.FunctionType

@Composable
fun FunctionLabel(
    label: String,
    color: Color,
    type: FunctionType,
    onHide: (type: FunctionType) -> Unit,
    isVisible: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            modifier = Modifier.weight(1f)
        )

        println(isVisible)

        IconButton(onClick = { onHide(type) }) {
            Icon(
                imageVector = if (isVisible)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff,
                contentDescription = if (isVisible) "Скрыть" else "Показать",
                tint = color
            )
        }
    }
}

@Composable
fun FunctionLabel(
    label: String,
    color: Color,
) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        color = color,
    )
}

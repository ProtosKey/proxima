package proxima.app.view.feature.result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import proxima.app.data.model.FunctionType
import proxima.app.theme.LocalAppDimens

@Composable
fun FunctionLabel(
    label: String,
    color: Color,
    type: FunctionType,
    onHide: (type: FunctionType) -> Unit,
    isVisible: Boolean,
    isTheBest: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(
                LocalAppDimens.current.paddingTiny
            )
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = color,
            )

            if (isTheBest) {
                Icon(
                    modifier = Modifier.fillMaxHeight(),
                    imageVector = Icons.Default.Star,
                    contentDescription = "Лучшее приближение",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

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

package interpolation.app.view.feature.input.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import interpolation.app.presentation.model.PointEntry
import interpolation.app.theme.LocalAppDimens

@Composable
fun Point(
    index: Int,
    point: PointEntry,
    canDelete: Boolean,
    onUpdate: (Int, String, String) -> Unit,
    onDelete: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            LocalAppDimens.current.paddingSmall
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.size(LocalAppDimens.current.paddingLarge),
            text = "${index + 1}.",
            style = MaterialTheme.typography.bodyLarge,
        )

        OutlinedTextField(
            value = point.x,
            onValueChange = { onUpdate(index, it, point.y) },
            label = { Text("X") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        OutlinedTextField(
            value = point.y,
            onValueChange = { onUpdate(index, point.x, it) },
            label = { Text("Y") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        IconButton(
            onClick = { onDelete(index) },
            enabled = canDelete
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить",
                tint = if (canDelete)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.outline
            )
        }
    }
}

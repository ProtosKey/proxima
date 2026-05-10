package interpolation.app.view.feature.input.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            LocalAppDimens.current.paddingSmall
        ),
    ) {
        /*
        Text(
            modifier = Modifier.size(LocalAppDimens.current.paddingLarge),
            text = "${index + 1}.",
            style = MaterialTheme.typography.bodyLarge,
        )
         */
        OutlinedTextField(
            value = point.x,
            shape = RoundedCornerShape(LocalAppDimens.current.radiusMedium),
            onValueChange = { onUpdate(index, it, point.y) },
            placeholder = {
                PlaceHolder("X", index)
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )

        )

        OutlinedTextField(
            value = point.y,
            shape = RoundedCornerShape(LocalAppDimens.current.radiusMedium),
            onValueChange = { onUpdate(index, point.x, it) },
            placeholder = {
                PlaceHolder("Y", index)
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        )

        FilledIconButton(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            shape = RoundedCornerShape(LocalAppDimens.current.radiusMedium),
            onClick = { onDelete(index) },
            enabled = canDelete,
            colors =
                IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить",
                tint = if (canDelete)
                    MaterialTheme.colorScheme.onError
                else
                    MaterialTheme.colorScheme.background
            )
        }
    }
}

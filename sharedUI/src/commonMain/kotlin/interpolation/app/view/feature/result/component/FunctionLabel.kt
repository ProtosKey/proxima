package interpolation.app.view.feature.result.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun FunctionLabel(label: String, color: Color) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        color = color
    )
}

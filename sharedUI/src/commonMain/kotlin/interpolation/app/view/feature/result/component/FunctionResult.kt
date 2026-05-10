package interpolation.app.view.feature.result.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig
import interpolation.app.theme.LocalAppDimens
import androidx.compose.ui.platform.LocalClipboardManager

@Composable
fun FunctionResult(latex: String, copy: String = latex) {
    val clipboard = LocalClipboardManager.current
    val scroll = rememberScrollState()
    val dimens = LocalAppDimens.current
    val surfaceColor = MaterialTheme.colorScheme.surface

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusMedium),
        border = BorderStroke(
            width = dimens.strokeThin,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        color = surfaceColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.horizontalGradient(
                                0.8f to Color.Transparent,
                                1f to surfaceColor
                            )
                        )
                    }
                    .horizontalScroll(scroll)
                    .padding(horizontal = dimens.paddingMedium, vertical = dimens.paddingMedium),
                contentAlignment = Alignment.CenterStart
            ) {
                Latex(
                    latex = latex,
                    config = LatexConfig(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        clipboard.setText(AnnotatedString(copy))
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Копировать",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(dimens.iconMedium)
                )
            }
        }
    }
}

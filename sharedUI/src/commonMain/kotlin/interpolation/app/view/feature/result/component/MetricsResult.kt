package interpolation.app.view.feature.result.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig
import interpolation.app.presentation.state.ResultState
import interpolation.app.theme.LocalAppDimens

@Composable
fun MetricResult(
    label: String,
    value: String,
    modifier: Modifier,
) {
    val scroll = rememberScrollState()
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(
            color = MaterialTheme.colorScheme.outlineVariant,
            width = LocalAppDimens.current.strokeThin,
        ),
        shape = RoundedCornerShape(LocalAppDimens.current.radiusMedium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = LocalAppDimens.current.paddingMedium)
                    .padding(horizontal = LocalAppDimens.current.paddingMedium)
            ) {
                Latex(
                    latex = label,
                    config = LatexConfig(
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Box(
                modifier = Modifier
                    .horizontalScroll(scroll)
                    .padding(bottom = LocalAppDimens.current.paddingMedium)
                    .padding(horizontal = LocalAppDimens.current.paddingMedium)
            ) {
                Latex(
                    latex = value,
                    config = LatexConfig(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                )
            }
        }
    }
}

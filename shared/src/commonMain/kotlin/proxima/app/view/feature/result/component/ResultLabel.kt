package proxima.app.view.feature.result.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import proxima.app.data.model.FunctionResult
import proxima.app.data.model.FunctionType
import proxima.app.domain.model.Metrics
import proxima.app.presentation.state.ResultState
import proxima.app.presentation.tools.FunctionClipper
import proxima.app.presentation.tools.StringParser
import proxima.app.theme.LocalAppDimens

@Composable
fun ResultLabel(
    label: String,
    isTheBest: Boolean,
    results: FunctionResult,
    type: FunctionType,
    onHide: (type: FunctionType) -> Unit,
    isVisible: Boolean,
    state: ResultState
) {
    val border = if (isTheBest) {
        BorderStroke(
            LocalAppDimens.current.strokeThick,
            MaterialTheme.colorScheme.primary
        )
    } else {
        BorderStroke(
            LocalAppDimens.current.strokeThin,
            MaterialTheme.colorScheme.outlineVariant
        )
    }

    val container = if (isTheBest) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    when (results) {
        is FunctionResult.Success -> {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                border = border,
                shape = RoundedCornerShape(
                    LocalAppDimens.current.radiusMedium
                ),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = container
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = LocalAppDimens.current.paddingMedium,
                        end = LocalAppDimens.current.paddingMedium,
                        bottom = LocalAppDimens.current.paddingMedium,
                        top = LocalAppDimens.current.paddingSmall,
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        LocalAppDimens.current.paddingSmall
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        FunctionLabel(
                            label = label,
                            color = if (isTheBest)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface,
                            type = type,
                            onHide = onHide,
                            isVisible = isVisible,
                            isTheBest = isTheBest
                        )
                    }

                    FunctionResult(
                        latex = results.function.acceptVisitor(state.formatter),
                        copy = results.function.acceptVisitor(FunctionClipper)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            LocalAppDimens.current.paddingSmall
                        )
                    ) {
                        MetricResult(
                            label = "\\textsf{R}^{2} \\textsf{(Детерминация)}",
                            value = "\\textrm{${StringParser.prepareToString(results.metrics.determination)}}",
                            modifier = Modifier.weight(1f),
                        )

                        if (results.metrics is Metrics.Linear) {
                            MetricResult(
                                label = "\\textsf{r} \\textsf{(Пирсон)}",
                                value = "\\textrm{${StringParser.prepareToString(results.metrics.linear)}}",
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            LocalAppDimens.current.paddingSmall
                        )
                    ) {
                        MetricResult(
                            label = "\\sigma \\textsf{(Срендеквадратичное отклонение)}",
                            value = "\\textrm{${StringParser.prepareToString(results.metrics.sko)}}",
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        is FunctionResult.Error -> {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    LocalAppDimens.current.strokeThin,
                    MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(LocalAppDimens.current.radiusMedium),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = LocalAppDimens.current.paddingMedium,
                        vertical = LocalAppDimens.current.paddingSmall
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        LocalAppDimens.current.paddingTiny
                    )
                ) {
                    FunctionLabel(
                        label = label,
                        color = MaterialTheme.colorScheme.error
                    )

                    Text(text = results.message, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

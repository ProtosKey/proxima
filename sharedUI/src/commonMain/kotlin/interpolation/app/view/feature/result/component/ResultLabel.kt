package interpolation.app.view.feature.result.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import interpolation.app.data.model.FunctionResult
import interpolation.app.domain.model.Metrics
import interpolation.app.presentation.tools.FunctionClipper
import interpolation.app.presentation.tools.FunctionFormatter
import interpolation.app.presentation.tools.StringParser
import interpolation.app.theme.LocalAppDimens
import interpolation.app.view.feature.result.component.Constants.SIGN_APPROX
import interpolation.app.view.feature.result.component.Constants.SIGN_METRICS

data object Constants {
    const val SIGN_APPROX = 5
    const val SIGN_METRICS = 10
}

@Composable
fun ResultLabel(label: String, isTheBest: Boolean, result: FunctionResult) {
    val border = if (isTheBest) {
        BorderStroke(
            LocalAppDimens.current.strokeThick,
            MaterialTheme.colorScheme.primary
        )
    } else {
        BorderStroke(
            LocalAppDimens.current.strokeThin,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    }

    val container = if (isTheBest) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    when (result) {
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
                                MaterialTheme.colorScheme.onSurface
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

                    FunctionResult(
                        latex = result.function.acceptVisitor(FunctionFormatter),
                        copy = result.function.acceptVisitor(FunctionClipper))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            LocalAppDimens.current.paddingSmall
                        )
                    ) {
                        MetricResult(
                            label = "\\textsf{R}^{2} \\textsf{(Детерминация)}",
                            value = StringParser.prepareToString(
                                result.metrics.determination,
                                SIGN_METRICS
                            ),
                            modifier = Modifier.weight(1f),
                        )

                        if (result.metrics is Metrics.Linear) {
                            MetricResult(
                                label = "\\textsf{r} \\textsf{(Пирсон)}",
                                value = StringParser.prepareToString(
                                    result.metrics.linear,
                                    SIGN_METRICS
                                ),
                                modifier = Modifier.weight(1f),
                            )
                        }
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

                    Text(text = result.message, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

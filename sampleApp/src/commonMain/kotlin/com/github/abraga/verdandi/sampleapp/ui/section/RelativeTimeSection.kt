package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import com.github.abraga.verdandi.sampleapp.ui.component.AdjustmentStepButton
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseIcons
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSection
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSubCard

@Composable
fun RelativeTimeSection(
    now: VerdandiMoment,
    modifier: Modifier = Modifier
) {
    var referenceMoment by remember { mutableStateOf(VerdandiShowcaseUsages.getRelativeMoment()) }
    val referenceMomentFormatted = remember(referenceMoment) {
        VerdandiShowcaseUsages.formatDateShort(referenceMoment)
    }

    var maxUnits by remember { mutableStateOf(4) }
    var separator by remember { mutableStateOf(", ") }
    var lastSeparator by remember { mutableStateOf(" and ") }

    var onNowTemplate by remember { mutableStateOf("just now") }
    var onPastTemplate by remember { mutableStateOf("%s ago") }
    var onFutureTemplate by remember { mutableStateOf("in %s") }

    val relativeResult = remember(
        now,
        referenceMoment,
        maxUnits,
        separator,
        lastSeparator,
        onNowTemplate,
        onPastTemplate,
        onFutureTemplate
    ) {
        VerdandiShowcaseUsages.formatPastRelativeCustomized(
            moment = referenceMoment,
            units = maxUnits,
            unitSeparator = separator,
            lastUnitSeparator = lastSeparator,
            onNow = { onNowTemplate },
            onPast = { onPastTemplate.replace("%s", it) },
            onFuture = { onFutureTemplate.replace("%s", it) }
        )
    }

    ShowcaseSection(title = "Relative Time", modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Md)
        ) {
            ShowcaseSubCard(title = "Setup") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
                    ) {
                        RelativeLabeledSlot(
                            label = "Reference",
                            modifier = Modifier.weight(1f)
                        ) {
                            StepperField(
                                displayValue = referenceMomentFormatted,
                                onDecrement = { referenceMoment = VerdandiShowcaseUsages.subtractOneDay(referenceMoment) },
                                onIncrement = { referenceMoment = VerdandiShowcaseUsages.addOneDay(referenceMoment) }
                            )
                        }

                        RelativeLabeledSlot(
                            label = "Max Units",
                            modifier = Modifier.weight(1f)
                        ) {
                            StepperField(
                                displayValue = maxUnits.toString(),
                                onDecrement = { if (maxUnits > 1) maxUnits-- },
                                onIncrement = { if (maxUnits < 6) maxUnits++ }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
                    ) {
                        RelativeTemplateField(
                            label = "Separator",
                            value = separator,
                            placeholder = "e.g. , ",
                            onValueChange = { separator = it },
                            modifier = Modifier.weight(1f)
                        )
                        RelativeTemplateField(
                            label = "Last",
                            value = lastSeparator,
                            placeholder = "e.g.  and ",
                            onValueChange = { lastSeparator = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            ShowcaseSubCard(title = "Templates") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
                ) {
                    Text(
                        text = "Use %s as placeholder for the time value",
                        color = ShowcaseTokens.Palette.textMuted,
                        fontSize = ShowcaseTokens.Typography.LabelSmall
                    )

                    RelativeTemplateField(
                        label = "Now",
                        value = onNowTemplate,
                        placeholder = "e.g. just now",
                        onValueChange = { onNowTemplate = it }
                    )
                    RelativeTemplateField(
                        label = "Past",
                        value = onPastTemplate,
                        placeholder = "e.g. %s ago",
                        onValueChange = { onPastTemplate = it }
                    )
                    RelativeTemplateField(
                        label = "Future",
                        value = onFutureTemplate,
                        placeholder = "e.g. in %s",
                        onValueChange = { onFutureTemplate = it }
                    )
                }
            }

            ShowcaseSubCard(title = "Result") {
                Text(
                    text = relativeResult,
                    color = ShowcaseTokens.Palette.accent,
                    fontSize = ShowcaseTokens.Typography.BodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun StepperField(
    displayValue: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AdjustmentStepButton(
            icon = ShowcaseIcons.Remove,
            onClick = onDecrement,
            contentDescription = "Decrease"
        )
        Text(
            text = displayValue,
            color = ShowcaseTokens.Palette.accent,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        AdjustmentStepButton(
            icon = ShowcaseIcons.Add,
            onClick = onIncrement,
            contentDescription = "Increase"
        )
    }
}

@Composable
private fun RelativeLabeledSlot(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Xs)
    ) {
        Text(
            text = label,
            color = ShowcaseTokens.Palette.textTertiary,
            fontSize = ShowcaseTokens.Typography.BodySmall
        )
        content()
    }
}

@Composable
private fun RelativeTemplateField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldShape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Sm) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Xs)
    ) {
        Text(
            text = label,
            color = ShowcaseTokens.Palette.textTertiary,
            fontSize = ShowcaseTokens.Typography.BodySmall
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = ShowcaseTokens.Palette.textPrimary,
                fontSize = ShowcaseTokens.Typography.BodySmall,
                fontFamily = FontFamily.Monospace
            ),
            cursorBrush = SolidColor(ShowcaseTokens.Palette.accent),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(fieldShape)
                        .background(ShowcaseTokens.Palette.glassSurface)
                        .border(1.dp, ShowcaseTokens.Palette.glassBorder, fieldShape)
                        .padding(ShowcaseTokens.Spacing.Sm)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = ShowcaseTokens.Palette.textMuted,
                            fontSize = ShowcaseTokens.Typography.BodySmall,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

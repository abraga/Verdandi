package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens

@Composable
fun PatternFormatEditor(
    pattern: String,
    referenceMoment: VerdandiMoment,
    onPatternChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldShape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Sm) }
    val resultShape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Sm) }

    val formattedResult: String? = remember(pattern, referenceMoment) {
        if (pattern.isEmpty()) {
            return@remember null
        }
        try {
            VerdandiShowcaseUsages.formatWithPattern(referenceMoment, pattern)
        } catch (_: Exception) {
            null
        }
    }
    val hasError = pattern.isNotEmpty() && formattedResult == null

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
    ) {
        Text(
            text = "Pattern",
            color = ShowcaseTokens.Palette.textTertiary,
            fontSize = ShowcaseTokens.Typography.BodySmall
        )

        BasicTextField(
            value = pattern,
            onValueChange = onPatternChange,
            singleLine = true,
            textStyle = TextStyle(
                color = ShowcaseTokens.Palette.textPrimary,
                fontSize = ShowcaseTokens.Typography.BodyMedium,
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
                    if (pattern.isEmpty()) {
                        Text(
                            text = "e.g. yyyy/MM/dd HH.mm.ss",
                            color = ShowcaseTokens.Palette.textMuted,
                            fontSize = ShowcaseTokens.Typography.BodyMedium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    innerTextField()
                }
            }
        )

        Text(
            text = "Tokens: yyyy  yy  MM  MMM  MMMM  dd  d  EEE  EEEE  HH  hh  mm  ss  SSS  a  Q  Z",
            color = ShowcaseTokens.Palette.textMuted,
            fontSize = ShowcaseTokens.Typography.LabelSmall
        )

        Text(
            text = "Result",
            color = ShowcaseTokens.Palette.textTertiary,
            fontSize = ShowcaseTokens.Typography.BodySmall
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(resultShape)
                .background(ShowcaseTokens.Palette.glassSurface)
                .border(1.dp, ShowcaseTokens.Palette.glassBorder, resultShape)
                .padding(ShowcaseTokens.Spacing.Sm)
        ) {
            when {
                hasError -> {
                    Text(
                        text = "Invalid pattern",
                        color = ShowcaseTokens.Palette.errorText,
                        fontSize = ShowcaseTokens.Typography.BodyMedium
                    )
                }
                else -> {
                    Text(
                        text = formattedResult ?: "",
                        color = ShowcaseTokens.Palette.accent,
                        fontSize = ShowcaseTokens.Typography.BodyMedium,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

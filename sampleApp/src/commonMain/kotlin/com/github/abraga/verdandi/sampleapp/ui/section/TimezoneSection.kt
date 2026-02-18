package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.unit.dp
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseInfoRow
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSection

@Composable
fun TimezoneSection(
    now: VerdandiMoment,
    modifier: Modifier = Modifier
) {
    val timezones = remember {
        mutableStateListOf(
            "UTC" to "UTC",
            "New York" to "America/New_York",
            "London" to "Europe/London",
            "Tokyo" to "Asia/Tokyo",
            "Sydney" to "Australia/Sydney"
        )
    }
    var newTimezoneInput by remember { mutableStateOf("") }
    var timezoneError by remember { mutableStateOf<String?>(null) }

    val timezoneDisplays by remember(now) {
        derivedStateOf {
            timezones.map { (label, zoneId) ->
                val formattedTime = try {
                    val converted = VerdandiShowcaseUsages.convertToTimeZone(now, zoneId)
                    VerdandiShowcaseUsages.formatDateTimeSimple(converted)
                } catch (_: Exception) {
                    "Unsupported"
                }
                Triple(label, zoneId, formattedTime)
            }
        }
    }

    val inputShape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Sm) }
    val addButtonShape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Sm) }

    ShowcaseSection(title = "Timezones", modifier = modifier) {
        timezoneDisplays.forEach { (label, _, formattedTime) ->
            ShowcaseInfoRow(label = label, value = formattedTime)
        }

        Spacer(modifier = Modifier.height(ShowcaseTokens.Spacing.Xs))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = newTimezoneInput,
                onValueChange = {
                    newTimezoneInput = it
                    timezoneError = null
                },
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
                            .clip(inputShape)
                            .background(ShowcaseTokens.Palette.glassSurface)
                            .border(1.dp, ShowcaseTokens.Palette.glassBorder, inputShape)
                            .padding(ShowcaseTokens.Spacing.Sm)
                    ) {
                        if (newTimezoneInput.isEmpty()) {
                            Text(
                                text = "e.g. Europe/Paris",
                                color = ShowcaseTokens.Palette.textMuted,
                                fontSize = ShowcaseTokens.Typography.BodySmall,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .clip(addButtonShape)
                    .background(ShowcaseTokens.Palette.accent.copy(alpha = 0.15f))
                    .border(1.dp, ShowcaseTokens.Palette.accent.copy(alpha = 0.4f), addButtonShape)
                    .clickable {
                        val id = newTimezoneInput.trim()
                        if (id.isNotEmpty()) {
                            try {
                                VerdandiShowcaseUsages.convertToTimeZone(now, id)
                                timezones.add(id to id)
                                newTimezoneInput = ""
                                timezoneError = null
                            } catch (_: Exception) {
                                timezoneError = "Unknown timezone: $id"
                            }
                        }
                    }
                    .padding(horizontal = ShowcaseTokens.Spacing.Md, vertical = ShowcaseTokens.Spacing.Sm),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add",
                    color = ShowcaseTokens.Palette.accent,
                    fontSize = ShowcaseTokens.Typography.BodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        val error = timezoneError
        if (error != null) {
            Text(
                text = error,
                color = ShowcaseTokens.Palette.errorText,
                fontSize = ShowcaseTokens.Typography.LabelSmall
            )
        }
    }
}

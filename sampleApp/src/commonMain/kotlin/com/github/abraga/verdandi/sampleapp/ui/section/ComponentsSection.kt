package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseInfoRow
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSection

@Composable
fun ComponentsSection(
    now: VerdandiMoment,
    modifier: Modifier = Modifier
) {
    var selectedField by remember { mutableStateOf(MomentComponentField.YEAR) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val componentValue = remember(now, selectedField) {
        resolveComponent(now, selectedField)
    }

    ShowcaseSection(title = "Components ($now)", modifier = modifier) {
        val selectorShape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Sm) }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(selectorShape)
                    .background(ShowcaseTokens.Palette.glassSurface)
                    .border(1.dp, ShowcaseTokens.Palette.glassBorder, selectorShape)
                    .clickable { dropdownExpanded = !dropdownExpanded }
                    .padding(ShowcaseTokens.Spacing.Sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedField.label,
                    color = ShowcaseTokens.Palette.textPrimary,
                    fontSize = ShowcaseTokens.Typography.BodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (dropdownExpanded) "▴" else "▾",
                    color = ShowcaseTokens.Palette.textTertiary,
                    fontSize = ShowcaseTokens.Typography.BodySmall
                )
            }

            AnimatedVisibility(visible = dropdownExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(selectorShape)
                        .background(ShowcaseTokens.Palette.glassSurface)
                        .border(1.dp, ShowcaseTokens.Palette.glassBorder, selectorShape)
                ) {
                    MomentComponentField.entries.forEach { field ->
                        Text(
                            text = field.label,
                            color = if (field == selectedField)
                                ShowcaseTokens.Palette.textPrimary
                            else
                                ShowcaseTokens.Palette.textSecondary,
                            fontSize = ShowcaseTokens.Typography.BodySmall,
                            fontWeight = if (field == selectedField) FontWeight.Medium else FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedField = field
                                    dropdownExpanded = false
                                }
                                .padding(ShowcaseTokens.Spacing.Sm)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(ShowcaseTokens.Spacing.Xs))

        ShowcaseInfoRow(
            label = selectedField.label,
            value = componentValue
        )
    }
}
private fun resolveComponent(moment: VerdandiMoment, field: MomentComponentField): String {
    return when (field) {
        MomentComponentField.YEAR -> moment.component.year.value.toString()
        MomentComponentField.MONTH -> "${moment.component.monthName} (${moment.component.month.value})"
        MomentComponentField.DAY -> moment.component.day.value.toString()
        MomentComponentField.DAY_OF_WEEK -> "${moment.component.dayOfWeekName} (${moment.component.dayOfWeek.value})"
        MomentComponentField.DAY_OF_YEAR -> moment.component.dayOfTheYear.value.toString()
        MomentComponentField.HOUR -> moment.component.hour.value.toString()
        MomentComponentField.MINUTE -> moment.component.minute.value.toString()
        MomentComponentField.SECOND -> moment.component.second.value.toString()
        MomentComponentField.MILLISECOND -> moment.component.millisecond.value.toString()
        MomentComponentField.MONTH_NAME -> moment.component.monthName
        MomentComponentField.MONTH_NAME_SHORT -> moment.component.monthNameShort
        MomentComponentField.DAY_OF_WEEK_NAME -> moment.component.dayOfWeekName
        MomentComponentField.DAY_OF_WEEK_NAME_SHORT -> moment.component.dayOfWeekNameShort
        MomentComponentField.IS_LEAP_YEAR -> if (moment.component.year.isLeapYear) "Yes" else "No"
        MomentComponentField.DAYS_IN_MONTH -> {
            val year = moment.component.year.value
            val month = moment.component.month
            VerdandiShowcaseUsages.getDaysInMonth(year, month).toString()
        }
        MomentComponentField.UTC_OFFSET -> moment.component.offset.toString().ifEmpty { "UTC+00:00" }
    }
}

private enum class MomentComponentField(val label: String) {
    YEAR("Year"),
    MONTH("Month"),
    DAY("Day of Month"),
    DAY_OF_WEEK("Day of Week"),
    DAY_OF_YEAR("Day of Year"),
    HOUR("Hour"),
    MINUTE("Minute"),
    SECOND("Second"),
    MILLISECOND("Millisecond"),
    MONTH_NAME("Month Name"),
    MONTH_NAME_SHORT("Month Name (Short)"),
    DAY_OF_WEEK_NAME("Day of Week Name"),
    DAY_OF_WEEK_NAME_SHORT("Day of Week Name (Short)"),
    IS_LEAP_YEAR("Is Leap Year"),
    DAYS_IN_MONTH("Days in Month"),
    UTC_OFFSET("UTC Offset")
}

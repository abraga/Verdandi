package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.abraga.verdandi.sampleapp.state.CalendarDay
import com.github.abraga.verdandi.sampleapp.state.CalendarState
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens

private val calendarDayShape = RoundedCornerShape(ShowcaseTokens.Radius.Sm)
private val selectedBgColor = ShowcaseTokens.Palette.accent.copy(alpha = 0.85f)
private val selectedBorderColor = ShowcaseTokens.Palette.accent
private val todayBorderColor = ShowcaseTokens.Palette.accent.copy(alpha = 0.45f)

@Composable
fun CalendarViewer(
    modifier: Modifier = Modifier,
    state: CalendarState,
    onPreviousMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    onToday: () -> Unit = {},
    onDayClick: ((Int) -> Unit)? = null
) {
    var epochSnapshot by remember { mutableLongStateOf(state.displayedMoment.inMilliseconds) }
    val currentEpoch = state.displayedMoment.inMilliseconds
    val isNavigatingForward = currentEpoch >= epochSnapshot

    SideEffect { epochSnapshot = currentEpoch }

    GlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationButton(
                    icon = ShowcaseIcons.ChevronLeft,
                    onClick = onPreviousMonth,
                    contentDescription = "Previous month"
                )

                AnimatedContent(
                    targetState = state.monthYear,
                    transitionSpec = {
                        val direction = if (isNavigatingForward) 1 else -1
                        (slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) { width -> width * direction } + fadeIn(
                            animationSpec = tween(200)
                        )).togetherWith(
                            slideOutHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) { width -> -width * direction } + fadeOut(
                                animationSpec = tween(150)
                            )
                        )
                    },
                    label = "monthYearTransition"
                ) { monthYear ->
                    Text(
                        text = monthYear,
                        color = ShowcaseTokens.Palette.textPrimary,
                        fontSize = ShowcaseTokens.Typography.TitleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(ShowcaseTokens.Radius.Sm))
                            .clickable(enabled = state.isViewingCurrentMonth.not()) { onToday() }
                            .padding(horizontal = ShowcaseTokens.Spacing.Sm)
                    )
                }

                NavigationButton(
                    icon = ShowcaseIcons.ChevronRight,
                    onClick = onNextMonth,
                    contentDescription = "Next month"
                )
            }

            Spacer(modifier = Modifier.height(ShowcaseTokens.Spacing.Md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                state.weekdayHeaders.forEach { header ->
                    Text(
                        text = header,
                        color = ShowcaseTokens.Palette.textTertiary,
                        fontSize = ShowcaseTokens.Typography.LabelSmall,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(ShowcaseTokens.Spacing.Sm))

            AnimatedContent(
                targetState = state.days,
                transitionSpec = {
                    val direction = if (isNavigatingForward) 1 else -1
                    (slideInHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) { width -> width * direction } + fadeIn(
                        animationSpec = tween(250)
                    )).togetherWith(
                        slideOutHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) { width -> -width * direction } + fadeOut(
                            animationSpec = tween(150)
                        )
                    )
                },
                label = "calendarGridTransition"
            ) { days ->
                Column {
                    val rows = days.chunked(7)
                    rows.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            week.forEach { day ->
                                CalendarDayCell(
                                    day = day,
                                    isSelected = day.isCurrentMonth && day.dayNumber == state.selectedDayNumber,
                                    onDayClick = onDayClick,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "buttonScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1f,
        animationSpec = tween(100),
        label = "buttonAlpha"
    )

    Box(
        modifier = modifier
            .size(36.dp)
            .scale(scale)
            .graphicsLayer { this.alpha = alpha }
            .clip(CircleShape)
            .background(ShowcaseTokens.Palette.glassSurface)
            .border(1.dp, ShowcaseTokens.Palette.glassBorder, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = ShowcaseTokens.Palette.textSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun CalendarDayCell(
    day: CalendarDay,
    isSelected: Boolean,
    onDayClick: ((Int) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val animSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    val selectedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = animSpec,
        label = "selectedAlpha"
    )

    val todayAlpha by animateFloatAsState(
        targetValue = if (day.isToday && !isSelected) 1f else 0f,
        animationSpec = animSpec,
        label = "todayAlpha"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.6f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "dayCellScale"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isSelected -> ShowcaseTokens.Palette.textPrimary
            day.isToday -> ShowcaseTokens.Palette.textPrimary
            day.isCurrentMonth -> ShowcaseTokens.Palette.textSecondary
            else -> ShowcaseTokens.Palette.textMuted
        },
        animationSpec = tween(300),
        label = "textColorAnim"
    )

    val fontWeight = if (isSelected || day.isToday) FontWeight.Bold else FontWeight.Normal

    val clickableModifier = if (day.isCurrentMonth && onDayClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onDayClick(day.dayNumber) }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .scale(scale)
            .then(clickableModifier),
        contentAlignment = Alignment.Center
    ) {
        if (selectedAlpha > 0f) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { alpha = selectedAlpha }
                    .clip(calendarDayShape)
                    .background(selectedBgColor)
                    .border(1.dp, selectedBorderColor, calendarDayShape)
            )
        }

        if (todayAlpha > 0f) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { alpha = todayAlpha }
                    .clip(calendarDayShape)
                    .border(1.dp, todayBorderColor, calendarDayShape)
            )
        }

        Text(
            text = day.dayNumber.toString(),
            color = textColor,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center
        )
    }
}

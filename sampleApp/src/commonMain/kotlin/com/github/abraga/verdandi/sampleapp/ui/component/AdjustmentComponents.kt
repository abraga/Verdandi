package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens

object ShowcaseIcons {

    val ChevronLeft: ImageVector by lazy {
        ImageVector.Builder(
            name = "ChevronLeft",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(15.41f, 7.41f)
                lineTo(14f, 6f)
                lineTo(8f, 12f)
                lineTo(14f, 18f)
                lineTo(15.41f, 16.59f)
                lineTo(10.83f, 12f)
                close()
            }
        }.build()
    }

    val ChevronRight: ImageVector by lazy {
        ImageVector.Builder(
            name = "ChevronRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(8.59f, 16.59f)
                lineTo(10f, 18f)
                lineTo(16f, 12f)
                lineTo(10f, 6f)
                lineTo(8.59f, 7.41f)
                lineTo(13.17f, 12f)
                close()
            }
        }.build()
    }

    val Add: ImageVector by lazy {
        ImageVector.Builder(
            name = "Add",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19f, 13f)
                horizontalLineTo(13f)
                verticalLineTo(19f)
                horizontalLineTo(11f)
                verticalLineTo(13f)
                horizontalLineTo(5f)
                verticalLineTo(11f)
                horizontalLineTo(11f)
                verticalLineTo(5f)
                horizontalLineTo(13f)
                verticalLineTo(11f)
                horizontalLineTo(19f)
                close()
            }
        }.build()
    }

    val Remove: ImageVector by lazy {
        ImageVector.Builder(
            name = "Remove",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19f, 13f)
                horizontalLineTo(5f)
                verticalLineTo(11f)
                horizontalLineTo(19f)
                close()
            }
        }.build()
    }
}

@Composable
fun AdjustmentStepButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val shape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Sm) }

    Box(
        modifier = modifier
            .clip(shape)
            .background(ShowcaseTokens.Palette.glassSurface)
            .border(1.dp, ShowcaseTokens.Palette.glassBorder, shape)
            .clickable { onClick() }
            .padding(horizontal = ShowcaseTokens.Spacing.Sm, vertical = ShowcaseTokens.Spacing.Xs),
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
fun AdjustmentRow(
    label: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AdjustmentStepButton(
            icon = ShowcaseIcons.Remove,
            onClick = onDecrement,
            contentDescription = "Decrease"
        )

        Text(
            text = label,
            color = ShowcaseTokens.Palette.textSecondary,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        AdjustmentStepButton(
            icon = ShowcaseIcons.Add,
            onClick = onIncrement,
            contentDescription = "Increase"
        )
    }
}

@Composable
fun AdjustmentButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Sm) }

    Box(
        modifier = modifier
            .clip(shape)
            .background(ShowcaseTokens.Palette.glassSurface)
            .border(1.dp, ShowcaseTokens.Palette.glassBorder, shape)
            .clickable { onClick() }
            .padding(vertical = ShowcaseTokens.Spacing.Sm),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = ShowcaseTokens.Palette.textSecondary,
            fontSize = ShowcaseTokens.Typography.LabelMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

package com.github.abraga.verdandi.sampleapp.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ShowcaseTokens {

    object Spacing {
        val Xs = 4.dp
        val Sm = 8.dp
        val Md = 16.dp
        val Xl = 32.dp
    }

    object Radius {
        val Sm = 8.dp
        val Md = 16.dp
        val Lg = 24.dp
    }

    object Typography {
        val DisplayLarge = 48.sp
        val TitleLarge = 24.sp
        val TitleSmall = 16.sp
        val BodyMedium = 14.sp
        val BodySmall = 12.sp
        val LabelMedium = 12.sp
        val LabelSmall = 10.sp
    }

    object Palette {
        val backgroundStart = Color(0xFF2C1C6E)
        val backgroundMid = Color(0xFF2C2C6E)
        val backgroundEnd = Color(0xFF18182E)

        val blobPrimary = Color(0xFF5E5CE6).copy(alpha = 0.08f)
        val blobSecondary = Color(0xFF0A84FF).copy(alpha = 0.06f)
        val blobTertiary = Color(0xFFBF5AF2).copy(alpha = 0.05f)

        val glassSurface = Color.White.copy(alpha = 0.06f)
        val glassBorder = Color.White.copy(alpha = 0.1f)

        val textPrimary = Color.White.copy(alpha = 0.92f)
        val textSecondary = Color.White.copy(alpha = 0.6f)
        val textTertiary = Color.White.copy(alpha = 0.4f)
        val textMuted = Color.White.copy(alpha = 0.25f)

        val accent = Color(0xFF0A84FF)
        val accentGlow = Color(0xFF0A84FF).copy(alpha = 0.3f)
        val errorText = Color(0xFFFF453A)
    }
}

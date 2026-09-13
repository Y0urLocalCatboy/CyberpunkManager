package com.example.cyberpunkmanager.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Black,
        fontSize = 64.sp,
        lineHeight = 60.sp,
        letterSpacing = (-0.03).sp,
        shadow = Shadow(
            color = CyberCyan.copy(alpha = 0.8f),
            blurRadius = 25f
        )
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 27.sp,
        letterSpacing = 0.04.sp,
        shadow = Shadow(
            color = CyberPink.copy(alpha = 0.6f),
            blurRadius = 15f
        )
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 0.08.sp,
        shadow = Shadow(
            color = CyberCyan.copy(alpha = 0.6f),
            blurRadius = 10f
        )
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 0.22.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp,
        letterSpacing = 0.14.sp
    )
)

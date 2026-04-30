package com.example.medcore.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Using system default font — swap FontFamily here once you add Lexend to res/font/
val MedCoreTypography = Typography(
    displayLarge   = TextStyle(fontWeight = FontWeight.Bold,     fontSize = 48.sp, lineHeight = 56.sp,  letterSpacing = (-1).sp),
    displayMedium  = TextStyle(fontWeight = FontWeight.Bold,     fontSize = 36.sp, lineHeight = 44.sp),
    displaySmall   = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 36.sp),
    headlineLarge  = TextStyle(fontWeight = FontWeight.Bold,     fontSize = 24.sp, lineHeight = 32.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp),
    headlineSmall  = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 26.sp),
    titleLarge     = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp),
    titleMedium    = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 22.sp),
    titleSmall     = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 12.sp, lineHeight = 18.sp),
    bodyLarge      = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 26.sp),
    bodyMedium     = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 22.sp),
    bodySmall      = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 18.sp),
    labelLarge     = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, letterSpacing = 0.5.sp),
    labelMedium    = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 12.sp, letterSpacing = 0.5.sp),
    labelSmall     = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 10.sp, letterSpacing = 0.8.sp),
)
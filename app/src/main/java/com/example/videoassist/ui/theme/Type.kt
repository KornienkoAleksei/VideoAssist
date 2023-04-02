package com.example.videoassist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.videoassist.R

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik)),
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik)),
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik)),
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik_medium)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik_medium)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik_medium)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 2.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.4.sp,
    ),
)
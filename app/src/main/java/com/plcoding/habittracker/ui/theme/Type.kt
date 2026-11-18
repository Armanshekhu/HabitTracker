package com.plcoding.habittracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.plcoding.habittracker.R

val Inter = FontFamily(
    Font(R.font.inter, FontWeight.Medium), // 500
    Font(R.font.inter, FontWeight.SemiBold), // 600
    Font(R.font.inter, FontWeight.Bold) // 700
)

val Manrope = FontFamily(
    Font(R.font.manrope, FontWeight.ExtraBold) // 800
)

<<<<<<< HEAD
val AppTypography = Typography(
=======
val Typography = Typography(
>>>>>>> 4becd9350ed6b5f76fdee7bae360b5479df6f69b
    displaySmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 1.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
)
<<<<<<< HEAD

val Typography = AppTypography
=======
>>>>>>> 4becd9350ed6b5f76fdee7bae360b5479df6f69b

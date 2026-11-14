package com.plcoding.habittracker.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.plcoding.habittracker.domain.model.HabitIcon

@Composable
fun HabitIconView(
    icon: HabitIcon,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = icon.iconRes),
        contentDescription = icon.displayName,
        tint = tint,
        modifier = modifier
    )
}

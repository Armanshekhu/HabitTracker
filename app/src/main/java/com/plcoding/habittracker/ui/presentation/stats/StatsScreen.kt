package com.plcoding.habittracker.ui.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.habittracker.R
import com.plcoding.habittracker.domain.model.HabitWithStreak
import com.plcoding.habittracker.ui.components.HabitIconView
import com.plcoding.habittracker.ui.theme.HabitTrackerTheme
import com.plcoding.habittracker.ui.theme.Inter
import com.plcoding.habittracker.ui.theme.Manrope

@Composable
fun StatsScreen(
    state: StatsState,
    onNavigateBack: () -> Unit
) {
    val colors = HabitTrackerTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F15))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Top Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A1A2E))
                            .clickable { onNavigateBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = "Back",
                            tint = Color(0xFFE0E0E0),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = "Statistics",
                        fontFamily = Manrope,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }
            }

            // Summary Cards Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        label = "This Week",
                        value = "${state.thisWeekPercentage}%",
                        valueColor = Color(0xFFA855F7),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        label = "Best Streak",
                        value = "${state.bestStreak}",
                        valueColor = Color(0xFF34D399),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        label = "Active",
                        value = "${state.activeHabitsCount}",
                        valueColor = Color(0xFFF472B6),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Activity Heatmap
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Activity",
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    HeatmapCard(state.heatmapWeeks)
                }
            }

            // Streaks Section
            item {
                Text(
                    text = "Streaks",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            items(
                items = state.habitStreaks,
                key = { it.habit.id }
            ) { item ->
                StreakCard(item)
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A2E))
            .padding(14.dp)
    ) {
        Text(
            text = label,
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            color = Color(0xFF666680)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontFamily = Manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp,
            color = valueColor
        )
    }
}

@Composable
private fun HeatmapCard(heatmapWeeks: List<List<HeatmapDay>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        // Weekday Headers (M, T, W, T, F, S, S)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(28.dp)) // Offset for W1-W4 row labels
            val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dayLabels.forEach { label ->
                    Text(
                        text = label,
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        color = Color(0xFF666680),
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Heatmap 4 Rows
        heatmapWeeks.forEachIndexed { index, week ->
            val rowLabel = "W${index + 1}"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rowLabel,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    color = Color(0xFF666680),
                    modifier = Modifier.width(28.dp)
                )

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { day ->
                        HeatmapCell(day)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Legend: Less [5 squares] More
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Less",
                fontFamily = Inter,
                fontSize = 9.sp,
                color = Color(0xFF666680)
            )
            Spacer(modifier = Modifier.width(6.dp))
            LegendSquare(Color(0xFF252540))
            Spacer(modifier = Modifier.width(4.dp))
            LegendSquare(Color(0xFF6C63FF).copy(alpha = 0.3f))
            Spacer(modifier = Modifier.width(4.dp))
            LegendSquare(Color(0xFF6C63FF).copy(alpha = 0.6f))
            Spacer(modifier = Modifier.width(4.dp))
            LegendSquare(Color(0xFF6C63FF).copy(alpha = 0.85f))
            Spacer(modifier = Modifier.width(4.dp))
            LegendSquare(Color(0xFF6C63FF))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "More",
                fontFamily = Inter,
                fontSize = 9.sp,
                color = Color(0xFF666680)
            )
        }
    }
}

@Composable
private fun HeatmapCell(day: HeatmapDay) {
    val cellColor = when {
        day.isFuture -> Color(0xFF1A1A2E)
        day.completionRatio == 0f -> Color(0xFF252540)
        day.completionRatio <= 0.25f -> Color(0xFF6C63FF).copy(alpha = 0.3f)
        day.completionRatio <= 0.5f -> Color(0xFF6C63FF).copy(alpha = 0.6f)
        day.completionRatio <= 0.75f -> Color(0xFF6C63FF).copy(alpha = 0.85f)
        else -> Color(0xFF6C63FF)
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(cellColor)
            .then(
                when {
                    day.isToday -> Modifier.border(2.dp, Color(0xFFA855F7), RoundedCornerShape(6.dp))
                    day.isFuture -> Modifier.drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        val halfStroke = strokeWidth / 2f
                        val cornerRadius = 6.dp.toPx()
                        drawRoundRect(
                            color = Color(0xFF333350),
                            topLeft = Offset(halfStroke, halfStroke),
                            size = Size(
                                size.width - strokeWidth,
                                size.height - strokeWidth
                            ),
                            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                            style = Stroke(
                                width = strokeWidth,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(6f, 4f),
                                    0f
                                )
                            )
                        )
                    }
                    else -> Modifier
                }
            )
    )
}

@Composable
private fun LegendSquare(color: Color) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(color)
    )
}

@Composable
private fun StreakCard(item: HabitWithStreak) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A2E))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF252540)),
            contentAlignment = Alignment.Center
        ) {
            HabitIconView(
                icon = item.habit.icon,
                tint = Color(0xFFA855F7),
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = item.habit.name,
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${item.currentStreak}",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF34D399)
            )
            Text(
                text = "Best: ${item.bestStreak}",
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                color = Color(0xFF666680)
            )
        }
    }
}

package com.plcoding.habittracker.ui.presentation.today

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.habittracker.R
import com.plcoding.habittracker.domain.model.HabitWithStreak
import com.plcoding.habittracker.ui.components.HabitIconView
import com.plcoding.habittracker.ui.theme.HabitTrackerTheme
import com.plcoding.habittracker.ui.theme.Inter
import com.plcoding.habittracker.ui.theme.Manrope
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TodayScreen(
    state: TodayState,
    onEvent: (TodayEvent) -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    val colors = HabitTrackerTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.surfaceElevated.let { Color(0xFF0F0F15) })
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    val formattedDate = state.date.format(
                        DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault())
                    )
                    Text(
                        text = formattedDate,
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = colors.textTertiary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Today",
                        fontFamily = Manrope,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = colors.textPrimary
                    )
                }

                // Stats button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1A2E))
                        .clickable { onNavigateToStats() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_stats_bar_chart),
                        contentDescription = "Statistics",
                        tint = colors.accent.let { Color(0xFFA855F7) },
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Daily Progress Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily progress",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = colors.textTertiary
                    )
                    Text(
                        text = "${state.completedCount} / ${state.totalCount}",
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color(0xFFA855F7)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                val progress = if (state.totalCount > 0) {
                    state.completedCount.toFloat() / state.totalCount
                } else 0f
                val animatedProgress by animateFloatAsState(
                    targetValue = progress.coerceIn(0f, 1f),
                    label = "progress"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF1A1A2E))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF6C63FF))
                    )
                }
            }

            // Habit List or Empty State
            if (state.habits.isEmpty() && !state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No habits yet",
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap + to add your first habit",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = colors.textTertiary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = state.habits,
                        key = { it.habit.id }
                    ) { item ->
                        HabitCard(
                            item = item,
                            onCardClick = { onNavigateToEdit(item.habit.id) },
                            onToggleComplete = { onEvent(TodayEvent.ToggleCompletion(item.habit.id)) }
                        )
                    }

                    // Spacer for FAB clearance
                    item {
                        Spacer(modifier = Modifier.height(90.dp))
                    }
                }
            }
        }

        // FAB
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF6C63FF))
                .clickable { onNavigateToCreate() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Habit",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun HabitCard(
    item: HabitWithStreak,
    onCardClick: () -> Unit,
    onToggleComplete: () -> Unit
) {
    val colors = HabitTrackerTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A2E))
            .clickable { onCardClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left Icon Container (42x42dp, 12dp radius, #252540 bg)
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF252540)),
            contentAlignment = Alignment.Center
        ) {
            HabitIconView(
                icon = item.habit.icon,
                tint = Color(0xFFA855F7),
                modifier = Modifier.size(22.dp)
            )
        }

        // Habit info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.habit.name,
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (item.currentStreak > 0) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star_filled),
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "${item.currentStreak} day streak",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = Color(0xFFF59E0B)
                    )
                } else {
                    Text(
                        text = "No streak yet",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = Color(0xFF666680)
                    )
                }
            }
        }

        // Circular Checkbox (30dp)
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .then(
                    if (item.isCompletedToday) {
                        Modifier.background(Color(0xFF34D399))
                    } else {
                        Modifier.border(2.dp, Color(0xFF333350), CircleShape)
                    }
                )
                .clickable { onToggleComplete() },
            contentAlignment = Alignment.Center
        ) {
            if (item.isCompletedToday) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "Completed",
                    tint = Color(0xFF0F0F15),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

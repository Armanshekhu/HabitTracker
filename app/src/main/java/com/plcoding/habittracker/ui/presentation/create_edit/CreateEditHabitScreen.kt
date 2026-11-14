package com.plcoding.habittracker.ui.presentation.create_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.habittracker.R
import com.plcoding.habittracker.domain.model.HabitIcon
import com.plcoding.habittracker.ui.components.HabitIconView
import com.plcoding.habittracker.ui.theme.HabitTrackerTheme
import com.plcoding.habittracker.ui.theme.Inter
import com.plcoding.habittracker.ui.theme.Manrope
import java.time.DayOfWeek

@Composable
fun CreateEditHabitScreen(
    state: CreateEditHabitState,
    onEvent: (CreateEditHabitEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(state.isSaved, state.isDeleted) {
        if (state.isSaved || state.isDeleted) {
            onNavigateBack()
        }
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F15))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    text = if (state.isEditMode) "Edit Habit" else "New Habit",
                    fontFamily = Manrope,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = Color.White
                )
            }

            // Selected Icon Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1A1A2E))
                        .border(
                            width = 2.dp,
                            color = Color(0xFF6C63FF),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    HabitIconView(
                        icon = state.selectedIcon,
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "Tap to change icon",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = Color(0xFF6C63FF)
                )
            }

            // Choose Icon Grid (all 20 icons)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1A1A2E))
                    .padding(14.dp)
            ) {
                Text(
                    text = "CHOOSE ICON",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    color = Color(0xFF666680)
                )

                Spacer(modifier = Modifier.height(10.dp))

                val allIcons = HabitIcon.entries
                // Render 4 rows of 5 icons
                allIcons.chunked(5).forEach { rowIcons ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowIcons.forEach { icon ->
                            val isSelected = icon == state.selectedIcon
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) Color(0xFF6C63FF) else Color(0xFF252540)
                                    )
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(
                                                2.dp,
                                                Color(0xFF8B7BFF),
                                                RoundedCornerShape(12.dp)
                                            )
                                        } else Modifier
                                    )
                                    .clickable {
                                        onEvent(CreateEditHabitEvent.IconSelected(icon))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                HabitIconView(
                                    icon = icon,
                                    tint = if (isSelected) Color.White else Color(0xFFA855F7),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Name Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "NAME",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    color = Color(0xFF666680)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1A2E))
                        .border(
                            width = 1.dp,
                            color = if (state.nameError != null) Color(0xFFEF4444) else Color(0xFF333350),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    if (state.name.isEmpty()) {
                        Text(
                            text = "e.g., Morning Run",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color(0xFF666680)
                        )
                    }
                    BasicTextField(
                        value = state.name,
                        onValueChange = { onEvent(CreateEditHabitEvent.NameChanged(it)) },
                        textStyle = TextStyle(
                            fontFamily = Inter,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.White
                        ),
                        cursorBrush = SolidColor(Color(0xFF6C63FF)),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (state.nameError != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.nameError,
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        color = Color(0xFFEF4444)
                    )
                }
            }

            // Repeat On (Day Picker)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "REPEAT ON",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    color = Color(0xFF666680)
                )

                Spacer(modifier = Modifier.height(8.dp))

                val dayList = listOf(
                    DayOfWeek.MONDAY to "M",
                    DayOfWeek.TUESDAY to "T",
                    DayOfWeek.WEDNESDAY to "W",
                    DayOfWeek.THURSDAY to "T",
                    DayOfWeek.FRIDAY to "F",
                    DayOfWeek.SATURDAY to "S",
                    DayOfWeek.SUNDAY to "S"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dayList.forEach { (day, label) ->
                        val isSelected = state.selectedDays.contains(day)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) Color(0xFF2E2E4A) else Color(0xFF1A1A2E)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF6C63FF) else Color(0xFF333350),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    onEvent(CreateEditHabitEvent.ToggleDay(day))
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontFamily = Inter,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (isSelected) Color(0xFFA855F7) else Color(0xFF666680)
                            )
                        }
                    }
                }

                if (state.daysError != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.daysError,
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        color = Color(0xFFEF4444)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CTA Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF6C63FF))
                    .clickable { onEvent(CreateEditHabitEvent.SaveHabit) }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.isEditMode) "Save Changes" else "Save Habit",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            // Secondary Action
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (state.isEditMode) {
                            onEvent(CreateEditHabitEvent.ShowDeleteDialog(true))
                        } else {
                            onNavigateBack()
                        }
                    }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.isEditMode) "Delete Habit" else "Discard Habit",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = Color(0xFFEF4444)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Delete Confirmation Dialog
        if (state.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { onEvent(CreateEditHabitEvent.ShowDeleteDialog(false)) },
                containerColor = Color(0xFF1A1A2E),
                title = {
                    Text(
                        text = "Delete Habit?",
                        fontFamily = Manrope,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete this habit and all its completion records? This action cannot be undone.",
                        fontFamily = Inter,
                        color = Color(0xFFB0B0C0)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { onEvent(CreateEditHabitEvent.DeleteHabit) }
                    ) {
                        Text(
                            text = "Delete",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { onEvent(CreateEditHabitEvent.ShowDeleteDialog(false)) }
                    ) {
                        Text(
                            text = "Cancel",
                            fontFamily = Inter,
                            color = Color(0xFF666680)
                        )
                    }
                }
            )
        }
    }
}

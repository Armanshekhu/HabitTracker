package com.plcoding.habittracker.domain.model

import androidx.annotation.DrawableRes
import com.plcoding.habittracker.R

enum class HabitIcon(
    val id: String,
    val displayName: String,
    @param:DrawableRes val iconRes: Int
) {
    RUN("run", "Run", R.drawable.ic_habit_run),
    READ("read", "Read", R.drawable.ic_habit_read),
    WATER("water", "Water", R.drawable.ic_habit_water),
    MEDITATE("meditate", "Meditate", R.drawable.ic_habit_meditate),
    SLEEP("sleep", "Sleep", R.drawable.ic_habit_sleep),
    CODE("code", "Code", R.drawable.ic_habit_code),
    MUSIC("music", "Music", R.drawable.ic_habit_music),
    COOK("cook", "Cook", R.drawable.ic_habit_cook),
    JOURNAL("journal", "Journal", R.drawable.ic_habit_journal),
    GYM("gym", "Gym", R.drawable.ic_habit_gym),
    YOGA("yoga", "Yoga", R.drawable.ic_habit_yoga),
    WALK("walk", "Walk", R.drawable.ic_habit_walk),
    CYCLE("cycle", "Cycle", R.drawable.ic_habit_cycle),
    STUDY("study", "Study", R.drawable.ic_habit_study),
    NO_PHONE("no_phone", "No Phone", R.drawable.ic_habit_no_phone),
    VITAMINS("vitamins", "Vitamins", R.drawable.ic_habit_vitamins),
    LANGUAGE("language", "Language", R.drawable.ic_habit_language),
    GRATITUDE("gratitude", "Gratitude", R.drawable.ic_habit_gratitude),
    HEALTH("health", "Health", R.drawable.ic_habit_health),
    ORGANIZE("organize", "Organize", R.drawable.ic_habit_organize);

    companion object {
        fun fromId(id: String): HabitIcon {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: RUN
        }
    }
}

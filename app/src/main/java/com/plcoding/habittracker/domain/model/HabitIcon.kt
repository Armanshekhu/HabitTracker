package com.plcoding.habittracker.domain.model

enum class HabitIcon(val id: String, val displayName: String) {
    RUN("run", "Run"),
    READ("read", "Read"),
    WATER("water", "Water"),
    MEDITATE("meditate", "Meditate"),
    SLEEP("sleep", "Sleep"),
    CODE("code", "Code"),
    MUSIC("music", "Music"),
    COOK("cook", "Cook"),
    JOURNAL("journal", "Journal"),
    GYM("gym", "Gym"),
    YOGA("yoga", "Yoga"),
    WALK("walk", "Walk"),
    CYCLE("cycle", "Cycle"),
    STUDY("study", "Study"),
    NO_PHONE("no_phone", "No Phone"),
    VITAMINS("vitamins", "Vitamins"),
    LANGUAGE("language", "Language"),
    GRATITUDE("gratitude", "Gratitude"),
    HEALTH("health", "Health"),
    ORGANIZE("organize", "Organize");

    companion object {
        fun fromId(id: String): HabitIcon {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: RUN
        }
    }
}

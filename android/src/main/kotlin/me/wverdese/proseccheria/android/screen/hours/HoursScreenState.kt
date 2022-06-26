package me.wverdese.proseccheria.android.screen.hours

data class HoursScreenState(
    val startHours: String,
    val startHoursError: Boolean,
    val startMinutes: String,
    val startMinutesError: Boolean,

    val endHours: String,
    val endHoursError: Boolean,
    val endMinutes: String,
    val endMinutesError: Boolean,

    val totalHours: String,
    val totalMinutes: String
)

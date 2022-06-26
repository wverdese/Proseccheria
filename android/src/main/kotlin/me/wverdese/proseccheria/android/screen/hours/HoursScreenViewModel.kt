package me.wverdese.proseccheria.android.screen.hours

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.datetime.LocalTime

class HoursScreenViewModel : ViewModel() {
    var state: HoursScreenState by mutableStateOf(initialState())
        private set

    fun onInputChanged(startHours: String, startMinutes: String, endHours: String, endMinutes: String) {
        state = computeState(startHours, startMinutes, endHours, endMinutes)
    }

    fun computeState(startHours: String, startMinutes: String, endHours: String, endMinutes: String): HoursScreenState {
        val startHoursNumber = startHours.numberOrNull()?.inRangeOrNull(0..23)
        val startMinutesNumber = startMinutes.numberOrNull()?.inRangeOrNull(0..59)
        val endHoursNumber = endHours.numberOrNull()?.inRangeOrNull(0..23)
        val endMinutesNumber = endMinutes.numberOrNull()?.inRangeOrNull(0..59)

        var totalHours = " "
        var totalMinutes = " "
        var totalError = false

        if (
            !startHours.isEmptyOrBlank() &&
            startHoursNumber != null &&
            !startMinutes.isEmptyOrBlank() &&
            startMinutesNumber != null &&
            !endHours.isEmptyOrBlank() &&
            endHoursNumber != null &&
            !endMinutes.isEmptyOrBlank() &&
            endMinutesNumber != null
        ) {
            val startMillis = LocalTime(hour = startHoursNumber, minute = startMinutesNumber).toMillisecondOfDay()
            val endMillis = LocalTime(hour = endHoursNumber, minute = endMinutesNumber).toMillisecondOfDay()
            if (startMillis <= endMillis) {
                val totalTime = LocalTime.fromMillisecondOfDay(endMillis - startMillis)
                totalHours = "%02d".format(totalTime.hour)
                totalMinutes = "%02d".format(totalTime.minute)
            } else {
                totalError = true
            }
        }

        return HoursScreenState(
            startHours = startHours,
            startHoursError = startHoursNumber == null || totalError,
            startMinutes = startMinutes,
            startMinutesError = startMinutesNumber == null || totalError,
            endHours = endHours,
            endHoursError = endHoursNumber == null,
            endMinutes = endMinutes,
            endMinutesError = endMinutesNumber == null,
            totalHours = totalHours,
            totalMinutes = totalMinutes
        )
    }

    private fun initialState() = computeState("", "", "", "")
}

private fun String.isEmptyOrBlank() = isEmpty() || isBlank()

private fun String.numberOrNull() =
    if (isEmptyOrBlank()) 0 else try {
        toInt()
    } catch (nfe: NumberFormatException) {
        null
    }

private fun Int.inRangeOrNull(range: IntRange) = if (this in range) this else null

package lv.luhmirins.folk.ui.screens.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lv.luhmirins.folk.data.prefs.Preferences
import lv.luhmirins.folk.domain.CalendarRepository
import lv.luhmirins.folk.domain.model.CalendarDate
import lv.luhmirins.folk.domain.model.ErrorType
import lv.luhmirins.folk.domain.model.HolidayType
import lv.luhmirins.folk.domain.model.HolidaysResult
import lv.luhmirins.folk.ui.screens.calendar.items.CalendarDateItem
import lv.luhmirins.folk.ui.screens.calendar.items.HolidayItems
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository,
    @Named("prettyDateFormat") private val dateFormatter: DateTimeFormatter,
    @Named("weekDayPrettyDateFormat") private val weekDayPrettyDateFormat: DateTimeFormatter,
    private val preferences: Preferences,
) : ViewModel() {

    private var currentDate by mutableStateOf<LocalDate>(LocalDate.now())
    private var startWeekFrom by mutableStateOf(DayOfWeek.MONDAY)

    var dateRange by mutableStateOf("")
    var holidays by mutableStateOf<List<CalendarDateItem>>(emptyList())
    var error by mutableStateOf<String?>(null)

    init {
        startWeekFrom = preferences.startWeekFrom
        loadHolidays(LocalDate.now())
    }

    fun setWeekStart(dayOfWeek: DayOfWeek) {
        preferences.startWeekFrom = dayOfWeek
        startWeekFrom = dayOfWeek
        loadHolidays(currentDate)
    }

    fun loadCurrentWeek() {
        loadHolidays(LocalDate.now())
    }

    fun loadWeekBefore() {
        loadHolidays(currentDate.minusWeeks(1))
    }

    fun loadWeekAfter() {
        loadHolidays(currentDate.plusWeeks(1))
    }

    private fun loadHolidays(date: LocalDate) {
        viewModelScope.launch {
            currentDate = date.with(startWeekFrom)
            val start = currentDate
            val end = start.plusDays(6)
            dateRange = "${dateFormatter.format(start)} - ${dateFormatter.format(end)}"

            kotlin.runCatching {
                when (val result = repository.getHolidaysInRange(start, end)) {
                    is HolidaysResult.Error -> {
                        error = result.type.msg
                        holidays = emptyList()
                    }
                    is HolidaysResult.Success -> {
                        error = null
                        holidays = mapItems(currentDate, result.items)
                    }
                }
            }.onFailure { e ->
                e.printStackTrace()
                error = ErrorType.Unknown.msg
                holidays = emptyList()
                // TODO handle error in UI
            }
        }
    }

    private fun mapItems(
        currentDate: LocalDate,
        result: List<CalendarDate>
    ): List<CalendarDateItem> {
        val holidaysByDate = result.associate { it.date to it.holidays }

        return (0L..6L).map { currentDate.plusDays(it) }.map {
            CalendarDateItem(
                date = weekDayPrettyDateFormat.format(it),
                holidays = holidaysByDate[it]?.map { holiday ->
                    when (holiday.type) {
                        HolidayType.Folk -> HolidayItems.Folk(holiday.name)
                        HolidayType.Public -> HolidayItems.Public(holiday.name)
                    }
                } ?: emptyList()
            )
        }
    }
}

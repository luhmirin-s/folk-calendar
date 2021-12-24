package lv.luhmirins.folk.domain.model

sealed class HolidaysResult {

    data class Error(val type: ErrorType) : HolidaysResult()
    data class Success(val items: List<CalendarDate>) : HolidaysResult()
}

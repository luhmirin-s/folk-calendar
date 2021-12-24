package lv.luhmirins.folk.ui.screens.calendar.items

data class CalendarDateItem(
    val date: String,
    val holidays: List<HolidayItems> = emptyList(),
)

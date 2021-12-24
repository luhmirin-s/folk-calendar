package lv.luhmirins.folk.ui.screens.calendar.items

sealed class HolidayItems(
    open val name: String,
) {
    data class Folk(override val name: String) : HolidayItems(name)
    data class Public(override val name: String) : HolidayItems(name)
}
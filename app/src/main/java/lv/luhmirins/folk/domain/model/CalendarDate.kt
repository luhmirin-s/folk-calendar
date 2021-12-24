package lv.luhmirins.folk.domain.model

import java.time.LocalDate


data class CalendarDate(
    val date: LocalDate,
    val holidays: List<Holiday> = emptyList(),
)

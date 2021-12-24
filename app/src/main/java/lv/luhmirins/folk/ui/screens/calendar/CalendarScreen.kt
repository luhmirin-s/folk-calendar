package lv.luhmirins.folk.ui.screens.calendar

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import lv.luhmirins.folk.domain.model.CalendarDate
import lv.luhmirins.folk.domain.model.ErrorType


@Composable
fun CalendarScreen() {
    val vm = hiltViewModel<CalendarViewModel>()
    CalendarScreen(
        vm.holidays,
        vm.error,
    )
}

@Composable
fun CalendarScreen(
    holidays: List<CalendarDate>,
    error: ErrorType?,
) {
    // TODO show list contents or error
}

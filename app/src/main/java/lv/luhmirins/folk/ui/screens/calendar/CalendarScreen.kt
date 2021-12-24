package lv.luhmirins.folk.ui.screens.calendar

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import lv.luhmirins.folk.ui.screens.calendar.components.AppBar
import lv.luhmirins.folk.ui.screens.calendar.components.DateItem
import lv.luhmirins.folk.ui.screens.calendar.components.ErrorContainer
import lv.luhmirins.folk.ui.screens.calendar.components.WeekDaySelectionDialog
import lv.luhmirins.folk.ui.screens.calendar.items.CalendarDateItem


@Composable
fun CalendarScreen() {
    val vm = hiltViewModel<CalendarViewModel>()
    CalendarScreen(
        vm.dateRange,
        vm.holidays,
        vm.error,
        vm::loadWeekBefore,
        vm::loadCurrentWeek,
        vm::loadWeekAfter,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    selectedDate: String,
    holidays: List<CalendarDateItem> = emptyList(),
    error: String? = null,
    onBack: () -> Unit = {},
    onReset: () -> Unit = {},
    onForward: () -> Unit = {},
) {
    Scaffold(
        topBar = { AppBar(selectedDate, onBack, onReset, onForward) },
    ) {
        if (error != null) {
            ErrorContainer(error)
        }
        LazyColumn {
            items(holidays) { item ->
                DateItem(item)
            }
        }
    }
}


package lv.luhmirins.folk.ui.screens.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lv.luhmirins.folk.ui.screens.calendar.components.AppBar
import lv.luhmirins.folk.ui.screens.calendar.components.DateItem
import lv.luhmirins.folk.ui.screens.calendar.components.ErrorContainer
import lv.luhmirins.folk.ui.screens.calendar.components.WeekDaySelectionDialog
import lv.luhmirins.folk.ui.screens.calendar.items.CalendarDateItem
import java.time.DayOfWeek


@Composable
fun CalendarScreen() {
    val vm = hiltViewModel<CalendarViewModel>()
    CalendarScreen(
        vm.dateRange,
        vm.holidays,
        vm.error,
        vm::setWeekStart,
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
    onWeekDaySelection: (DayOfWeek) -> Unit = {},
    onBack: () -> Unit = {},
    onReset: () -> Unit = {},
    onForward: () -> Unit = {},
) {
    val openSelectionDialog = remember { mutableStateOf(false) }
    WeekDaySelectionDialog(
        openSelectionDialog,
        onSelected = onWeekDaySelection
    )

    Scaffold(
        topBar = { AppBar(selectedDate, onBack, onReset, onForward) },
    ) {
        if (error != null) {
            ErrorContainer(error)
        }
        Column {
            OutlinedButton(
                onClick = { openSelectionDialog.value = true },
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) { Text("Change first day of the week") }
            LazyColumn {
                items(holidays) { item ->
                    DateItem(item)
                }
            }
        }
    }
}

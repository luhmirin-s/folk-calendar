package lv.luhmirins.folk.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lv.luhmirins.folk.ui.screens.calendar.items.CalendarDateItem
import lv.luhmirins.folk.ui.screens.calendar.items.HolidayItems


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

@Composable
private fun AppBar(
    selectedDate: String,
    onBack: () -> Unit,
    onReset: () -> Unit,
    onForward: () -> Unit
) {
    MediumTopAppBar(
        title = { Text(selectedDate) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
        ),
        actions = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Week before"
                )
            }
            IconButton(onClick = onReset) {
                Icon(
                    imageVector = Icons.Filled.Today,
                    contentDescription = "Reset"
                )
            }
            IconButton(onClick = onForward) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Week after"
                )
            }
        }
    )
}

@Composable
private fun ErrorContainer(error: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.errorContainer
            )
            .padding(16.dp)
    ) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
private fun DateItem(item: CalendarDateItem) {
    val (containerColor, onContainerColor) = with(MaterialTheme.colorScheme) {
        when {
            item.holidays.isEmpty() -> surface to onSurface
            item.holidays.all { it is HolidayItems.Public } -> secondaryContainer to onSecondaryContainer
            item.holidays.all { it is HolidayItems.Folk } -> tertiaryContainer to onTertiaryContainer
            else -> tertiary to onTertiary
        }
    }

    val headlineStyle = if (item.holidays.isNotEmpty()) {
        MaterialTheme.typography.titleLarge
    } else {
        MaterialTheme.typography.titleMedium
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(shape = RoundedCornerShape(8.dp), color = containerColor)
            .padding(8.dp)
    ) {
        Text(
            text = item.date,
            style = headlineStyle,
            color = onContainerColor,
        )
        if (item.holidays.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            item.holidays.map { HolidayName(it, onContainerColor) }
        }
    }
}

@Composable
private fun HolidayName(
    item: HolidayItems,
    onContainerColor: Color
) {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = when (item) {
                is HolidayItems.Folk -> Icons.Filled.Celebration
                is HolidayItems.Public -> Icons.Filled.LocationCity
            },
            tint = onContainerColor,
            contentDescription = "Holiday!"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            color = onContainerColor,
        )
    }
}

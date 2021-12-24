package lv.luhmirins.folk.ui.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lv.luhmirins.folk.ui.screens.calendar.items.CalendarDateItem
import lv.luhmirins.folk.ui.screens.calendar.items.HolidayItems

@Composable
fun DateItem(item: CalendarDateItem) {
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
fun HolidayName(
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

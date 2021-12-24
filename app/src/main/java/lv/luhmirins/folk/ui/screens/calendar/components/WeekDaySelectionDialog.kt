package lv.luhmirins.folk.ui.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.DayOfWeek

@Composable
fun WeekDaySelectionDialog(
    openSelectionDialog: MutableState<Boolean>,
    onSelected: (DayOfWeek) -> Unit,
) {
    if (openSelectionDialog.value) {
        Dialog(
            onDismissRequest = {
                openSelectionDialog.value = false
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Start week with",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(16.dp))
                mapOf(
                    DayOfWeek.MONDAY to "Monday",
                    DayOfWeek.TUESDAY to "Tuesday",
                    DayOfWeek.WEDNESDAY to "Wednesday",
                    DayOfWeek.THURSDAY to "Thursday",
                    DayOfWeek.FRIDAY to "Friday",
                    DayOfWeek.SATURDAY to "Saturday",
                    DayOfWeek.SUNDAY to "Sunday",
                ).map { (dayOfWeek, name) ->
                    ClickableText(
                        text = AnnotatedString(name),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        onClick = {
                            openSelectionDialog.value = false
                            onSelected(dayOfWeek)

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                    )
                }
            }
        }
    }
}

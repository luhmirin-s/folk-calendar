package lv.luhmirins.folk.ui.screens.calendar.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun AppBar(
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

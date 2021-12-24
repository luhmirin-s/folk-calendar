package lv.luhmirins.folk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import lv.luhmirins.folk.ui.screens.calendar.CalendarScreen
import lv.luhmirins.folk.ui.theme.FolkCalendarTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FolkCalendarTheme {
                CalendarScreen()
            }
        }
    }
}

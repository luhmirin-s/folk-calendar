package lv.luhmirins.folk.ui.screens.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lv.luhmirins.folk.domain.CalendarRepository
import lv.luhmirins.folk.domain.model.CalendarDate
import lv.luhmirins.folk.domain.model.ErrorType
import lv.luhmirins.folk.domain.model.HolidaysResult
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var holidays by mutableStateOf<List<CalendarDate>>(emptyList())
    var error by mutableStateOf<ErrorType?>(null)

    init {
        loadHolidays(LocalDate.now())
    }

    private fun loadHolidays(date: LocalDate) {
        viewModelScope.launch {
            kotlin.runCatching {

                when (val result = repository.getHolidaysForDate(date)) {
                    is HolidaysResult.Error -> {
                        error = result.type
                        holidays = emptyList()
                    }
                    is HolidaysResult.Success -> {
                        error = null
                        holidays = result.items
                    }
                }
            }.onFailure { e ->
                e.printStackTrace()
                error = ErrorType.Unknown
                holidays = emptyList()
                // TODO handle error in UI
            }
        }
    }

}

package lv.luhmirins.folk.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import lv.luhmirins.folk.data.db.HolidayStore
import lv.luhmirins.folk.domain.model.HolidaysResult
import lv.luhmirins.folk.domain.usecases.FetchExtraDatesUseCase
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named


class CalendarRepository @Inject constructor(
    @Named("ioDispatcher") private val ioDispatcher: CoroutineDispatcher,
    private val fetchExtraDatesUseCase: FetchExtraDatesUseCase,
    private val store: HolidayStore,
) {

    suspend fun getHolidaysForDate(date: LocalDate): HolidaysResult =
        withContext(ioDispatcher) {
            val (minKnown, maxKnown) = store.getKnownDateRange()
            val extraData = fetchExtraDatesUseCase(date, minKnown, maxKnown)

            saveSuccesses(extraData)
            val weeksData = store.getWeekHolidays(date)

            val error = extraData.firstOrNull { it is HolidaysResult.Error }
            if (weeksData.isEmpty() && error != null) {
                return@withContext error
            }

            HolidaysResult.Success(weeksData)
        }

    private suspend fun saveSuccesses(extraData: List<HolidaysResult>) {
        extraData.filterIsInstance<HolidaysResult.Success>()
            .flatMap { it.items }
            .takeUnless { it.isEmpty() }
            ?.let { store.saveHolidays(it) }
    }
}

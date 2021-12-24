package lv.luhmirins.folk.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import lv.luhmirins.folk.data.db.HolidayStore
import lv.luhmirins.folk.domain.model.HolidaysResult
import lv.luhmirins.folk.domain.usecases.FetchExtraDatesUseCase
import lv.luhmirins.folk.domain.usecases.GetKnownDateRangeUseCase
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named


class CalendarRepository @Inject constructor(
    @Named("ioDispatcher") private val ioDispatcher: CoroutineDispatcher,
    private val fetchExtraDatesUseCase: FetchExtraDatesUseCase,
    private val getKnownDateRangeUseCase: GetKnownDateRangeUseCase,
    private val store: HolidayStore,
) {

    /**
     * Returns known holidays in the provided range.
     * If the range is close/over know date borders, extra data will be fetched.
     */
    suspend fun getHolidaysInRange(start: LocalDate, end: LocalDate): HolidaysResult =
        withContext(ioDispatcher) {
            val (minKnown, maxKnown) = getKnownDateRangeUseCase()
            val extraData = fetchExtraDatesUseCase(start, minKnown, maxKnown)

            saveSuccesses(extraData)

            val error = extraData.firstOrNull { it is HolidaysResult.Error }
            if (error != null) {
                return@withContext error
            }

            val weeksData = store.getWeekHolidays(start, end)
            HolidaysResult.Success(weeksData)
        }

    private suspend fun saveSuccesses(extraData: List<HolidaysResult>) {
        extraData.filterIsInstance<HolidaysResult.Success>()
            .flatMap { it.items }
            .takeUnless { it.isEmpty() }
            ?.let { store.saveHolidays(it) }
    }
}

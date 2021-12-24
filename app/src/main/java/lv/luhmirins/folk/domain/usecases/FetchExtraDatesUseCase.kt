package lv.luhmirins.folk.domain.usecases

import lv.luhmirins.folk.data.api.HolidaysApi
import lv.luhmirins.folk.data.api.models.HolidaysRequest
import lv.luhmirins.folk.data.prefs.Preferences
import lv.luhmirins.folk.domain.model.HolidaysResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

class FetchExtraDatesUseCase @Inject constructor(
    @Named("apiKey") private val apiKey: String,
    @Named("apiDateFormat") private val apiDateFormatter: DateTimeFormatter,
    private val api: HolidaysApi,
    private val mapHolidayResponseUseCase: MapHolidayResponseUseCase,
    private val prefs: Preferences,
) {

    /**
     * Checks if the provided `date` is reasonably close to known data boundaries and
     * when necessary fetches additional batches of data in that direction.
     */
    suspend operator fun invoke(
        date: LocalDate,
        minKnown: LocalDate,
        maxKnown: LocalDate
    ): List<HolidaysResult> {
        val result = mutableListOf<HolidaysResult>()
        // If date is close to lower bounds of the known range
        if (isCloseToKnownLowerBound(date, minKnown)) {
            val newMin = minKnown.minusDays(DAYS_TO_FETCH)
            val dates = fetchDates(newMin, minKnown)
            if (dates is HolidaysResult.Success) {
                prefs.minKnownDate = apiDateFormatter.format(newMin)
            }
            result.add(dates)
        } else if (isCloseToKnownUpperBound(date, maxKnown)) {
            // If date is close to upper bounds of known dates
            val newMax = maxKnown.plusDays(DAYS_TO_FETCH)
            val dates = fetchDates(maxKnown, newMax)
            if (dates is HolidaysResult.Success) {
                prefs.maxKnownDate = apiDateFormatter.format(newMax)
            }
            result.add(dates)
        }
        return result
    }

    private fun isCloseToKnownLowerBound(date: LocalDate, minKnown: LocalDate) = date
        .minusDays(7)
        .isBefore(minKnown)

    private fun isCloseToKnownUpperBound(date: LocalDate, maxKnown: LocalDate) = date
        .plusDays(7)
        .isAfter(maxKnown)

    private suspend fun fetchDates(start: LocalDate, end: LocalDate): HolidaysResult {
        val response = api.getHolidays(
            HolidaysRequest(
                apiKey = apiKey,
                startDate = apiDateFormatter.format(start),
                endDate = apiDateFormatter.format(end)
            )
        )
        return mapHolidayResponseUseCase(response)
    }

    companion object {
        private const val DAYS_TO_FETCH = 29L
    }
}

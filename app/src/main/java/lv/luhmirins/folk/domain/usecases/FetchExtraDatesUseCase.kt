package lv.luhmirins.folk.domain.usecases

import lv.luhmirins.folk.data.api.HolidaysApi
import lv.luhmirins.folk.data.api.models.HolidaysRequest
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
) {

    suspend operator fun invoke(
        date: LocalDate,
        minKnown: LocalDate,
        maxKnown: LocalDate
    ): List<HolidaysResult> {
        val result = mutableListOf<HolidaysResult>()
        // If date is close to lower bounds of the known range
        if (isCloseToKnownLowerBound(date, minKnown)) {
            result.add(fetchDates(minKnown.minusDays(29), minKnown))

        } else if (isCloseToKnownUpperBound(date, maxKnown)) {
            // If date is close to upper bounds of known dates
            result.add(fetchDates(minKnown.minusDays(29), minKnown))
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
}
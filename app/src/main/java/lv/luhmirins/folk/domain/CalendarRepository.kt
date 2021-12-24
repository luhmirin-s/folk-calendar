package lv.luhmirins.folk.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import lv.luhmirins.folk.data.api.HolidaysApi
import lv.luhmirins.folk.data.api.models.HolidaysRequest
import lv.luhmirins.folk.domain.model.HolidaysResult
import lv.luhmirins.folk.domain.usecases.MapHolidayResponseUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named


class CalendarRepository @Inject constructor(
    @Named("apiKey") private val apiKey: String,
    @Named("ioDispatcher") private val ioDispatcher: CoroutineDispatcher,
    @Named("apiDateFormat") private val apiDateFormatter: DateTimeFormatter,
    private val api: HolidaysApi,
    private val mapHolidayResponseUseCase: MapHolidayResponseUseCase,
) {

    suspend fun getHolidaysForDate(date: LocalDate): HolidaysResult = withContext(ioDispatcher) {
        val start = date.minusDays(14)
        val end = start.plusDays(14)

        val body = HolidaysRequest(
            apiKey = apiKey,
            startDate = apiDateFormatter.format(start),
            endDate = apiDateFormatter.format(end)
        )

        val response = api.getHolidays(body)

        mapHolidayResponseUseCase(response)
    }
}

package lv.luhmirins.folk.domain.usecases

import lv.luhmirins.folk.data.api.models.HolidaysResponse
import lv.luhmirins.folk.domain.model.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

class MapHolidayResponseUseCase @Inject constructor(
    @Named("apiDateFormat") private val apiDateFormatter: DateTimeFormatter,
) {

    /**
     * Maps API response to domain models.
     */
    operator fun invoke(response: HolidaysResponse) = if (response.error) {
        HolidaysResult.Error(ErrorType.fromString(response.reason))
    } else {
        val dates = response.holidays?.map { (k, v) ->
            CalendarDate(
                date = LocalDate.from(apiDateFormatter.parse(k)),
                holidays = v.map {
                    Holiday(
                        name = it.name,
                        type = HolidayType.fromString(it.type)
                    )
                }
            )
        }?.sortedBy { it.date }

        HolidaysResult.Success(dates ?: emptyList())
    }
}

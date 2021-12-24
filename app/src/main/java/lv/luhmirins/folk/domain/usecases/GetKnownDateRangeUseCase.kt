package lv.luhmirins.folk.domain.usecases

import lv.luhmirins.folk.data.prefs.Preferences
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

class GetKnownDateRangeUseCase @Inject constructor(
    @Named("apiDateFormat") private val apiDateFormatter: DateTimeFormatter,
    private val prefs: Preferences,
) {
    suspend operator fun invoke(): Pair<LocalDate, LocalDate> {
        val min = prefs.minKnownDate
            ?.let { LocalDate.from(apiDateFormatter.parse(it)) }
            ?: LocalDate.now()

        val max = prefs.maxKnownDate
            ?.let { LocalDate.from(apiDateFormatter.parse(it)) }
            ?: LocalDate.now()

        return min to max
    }

}
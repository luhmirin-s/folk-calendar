package lv.luhmirins.folk.data.db

import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import lv.luhmirins.folk.domain.model.CalendarDate
import lv.luhmirins.folk.domain.model.Holiday
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class HolidayStore @Inject constructor(
    private val realmConfig: RealmConfiguration,
    @Named("realmDispatcher") private val dispatcher: CoroutineDispatcher
) {

    suspend fun saveHolidays(dates: List<CalendarDate>) = withRealm { realm ->
        val holidays = dates.flatMap { calendarDate ->
            calendarDate.holidays.map { it.toObject(calendarDate) }
        }
        realm.executeTransaction { it.insertOrUpdate(holidays) }
    }

    suspend fun getKnownDateRange(): Pair<LocalDate, LocalDate> = withRealm { realm ->
        val min = realm.where(HolidayObject::class.java)
            .minimumDate("date")
            ?.let { dateToLocalDate(it) }
            ?: LocalDate.now()

        val max = realm.where(HolidayObject::class.java)
            .maximumDate("date")
            ?.let { dateToLocalDate(it) }
            ?: LocalDate.now()

        min to max
    }

    suspend fun getWeekHolidays(weekStart: LocalDate): List<CalendarDate> = withRealm { realm ->
        val startDate = localDateToDate(weekStart)
        val endDate = localDateToDate(weekStart.plusDays(7))

        val items = realm.where(HolidayObject::class.java)
            .between("date", startDate, endDate)
            .findAll()

        items.groupBy { it.date }.map { (date, holidays) -> fromObject(date, holidays) }
    }

    /**
     * Convenience wrapper to enforce realm threading requirements in all calls.
     */
    private suspend inline fun <T> withRealm(crossinline block: suspend (Realm) -> T) =
        withContext(dispatcher) {
            block(Realm.getInstance(realmConfig))
        }

    private fun Holiday.toObject(calendarDate: CalendarDate) = HolidayObject().also {
        it.name = name
        it.date = localDateToDate(calendarDate.date)
        it.type = this@toObject.type
    }

    private fun fromObject(date: Date, holidays: List<HolidayObject>) = CalendarDate(
        date = dateToLocalDate(date),
        holidays = holidays.map { Holiday(it.name, it.type) }
    )

    // Since Realm supports java.uti.Date and in app I use java.time.LocalDate dates
    // have to converted between different formats

    private fun localDateToDate(date: LocalDate) =
        Date.from(Instant.from(date.atStartOfDay(ZoneId.systemDefault())))

    private fun dateToLocalDate(date: Date) =
        LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault()))
}

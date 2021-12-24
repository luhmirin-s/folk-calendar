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
import javax.inject.Singleton


@Singleton
class HolidayStore @Inject constructor(
    private val realmConfig: RealmConfiguration,
    @Named("realmDispatcher") private val dispatcher: CoroutineDispatcher
) {

    /**
     * Upserts provided holiday data.
     */
    suspend fun saveHolidays(dates: List<CalendarDate>) = withRealm { realm ->
        val holidays = dates.flatMap { calendarDate ->
            calendarDate.holidays.map { it.toObject(calendarDate) }
        }
        realm.executeTransaction { it.insertOrUpdate(holidays) }
    }

    /**
     * Returns all saved holiday records in the provided date range.
     */
    suspend fun getWeekHolidays(start: LocalDate, end: LocalDate): List<CalendarDate> =
        withRealm { realm ->
            val startDate = localDateToDate(start)
            val endDate = localDateToDate(end)

            val items = realm.where(HolidayObject::class.java)
                .between("date", startDate, endDate)
                .findAll()

            items.groupBy { it.date }
                .map { (date, holidays) -> fromObject(date, holidays) }
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

    // Since Realm supports java.uti.Date and in app I use java.time.LocalDate,
    // dates have to converted between different formats on the fly

    private fun localDateToDate(date: LocalDate) =
        Date.from(Instant.from(date.atStartOfDay(ZoneId.systemDefault())))

    private fun dateToLocalDate(date: Date) =
        LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault()))
}

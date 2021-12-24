package lv.luhmirins.folk.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import java.time.DayOfWeek
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(
    @Named("localPrefs") private val localPrefs: SharedPreferences,
) {

    /**
     * Lowest known date that was ever fetched. Stored in yyyy-MM-dd format
     */
    var minKnownDate: String?
        get() = localPrefs.getString(KEY_MIN_DATE, null)
        set(v) {
            localPrefs.edit { putString(KEY_MIN_DATE, v) }
        }

    /**
     * Highest known date that was ever fetched. Stored in yyyy-MM-dd format
     */
    var maxKnownDate: String?
        get() = localPrefs.getString(KEY_MAX_DATE, null)
        set(v) {
            localPrefs.edit { putString(KEY_MAX_DATE, v) }
        }

    /**
     * First day of the week to be displayed in calendar. Defaults to MONDAY.
     */
    var startWeekFrom: DayOfWeek
        get() = localPrefs.getString(KEY_WEEK_START, null)
            ?.let { DayOfWeek.valueOf(it) }
            ?: DayOfWeek.MONDAY
        set(v) {
            localPrefs.edit { putString(KEY_WEEK_START, v.name) }
        }

    companion object {
        private const val KEY_MIN_DATE = "minKnownDate"
        private const val KEY_MAX_DATE = "maxKnownDate"
        private const val KEY_WEEK_START = "startWeekFrom"
    }
}
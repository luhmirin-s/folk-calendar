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

    var minKnownDate: String?
        get() = localPrefs.getString(KEY_MIN_DATE, null)
        set(v) {
            localPrefs.edit { putString(KEY_MIN_DATE, v) }
        }

    var maxKnownDate: String?
        get() = localPrefs.getString(KEY_MAX_DATE, null)
        set(v) {
            localPrefs.edit { putString(KEY_MAX_DATE, v) }
        }

    var startWeekFrom: DayOfWeek
        get() = localPrefs.getString(KEY_WEEK_START, null)
            ?.let { DayOfWeek.valueOf(it) }
            ?: DayOfWeek.MONDAY
        set(v) {
            localPrefs.edit { putString(KEY_WEEK_START, v.name) }
        }

    companion object {
        const val KEY_MIN_DATE = "minKnownDate"
        const val KEY_MAX_DATE = "maxKnownDate"
        const val KEY_WEEK_START = "startWeekFrom"
    }
}
package lv.luhmirins.folk.data.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import lv.luhmirins.folk.domain.model.HolidayType
import java.util.*

open class HolidayObject : RealmObject() {
    @PrimaryKey
    var name: String = ""

    @Required
    var date: Date = Date()

    @Required
    var typeString: String = HolidayType.Folk.name
    var type: HolidayType
        get() {
            return try {
                HolidayType.valueOf(typeString)
            } catch (e: IllegalArgumentException) {
                HolidayType.Folk
            }
        }
        set(value) {
            typeString = value.name
        }
}

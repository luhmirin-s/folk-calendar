package lv.luhmirins.folk.domain.model

enum class HolidayType {
    Public,
    Folk;

    companion object {
        fun fromString(type: String) = if (type == "folk") Folk else Public
    }
}
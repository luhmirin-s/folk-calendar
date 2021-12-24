package lv.luhmirins.folk.data.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HolidaysResponse(
    val error: Boolean,
    val reason: String? = null,
    val holidays: Map<String, List<HolidayDateResponse>>? = null,
)

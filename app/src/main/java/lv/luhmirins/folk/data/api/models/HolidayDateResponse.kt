package lv.luhmirins.folk.data.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HolidayDateResponse(
    val name: String,
    val type: String,
)

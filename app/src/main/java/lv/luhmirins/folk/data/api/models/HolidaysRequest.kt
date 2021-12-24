package lv.luhmirins.folk.data.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HolidaysRequest(
    val apiKey: String,
    val startDate: String,
    val endDate: String,
)

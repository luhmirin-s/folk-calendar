package lv.luhmirins.folk.data.api

import lv.luhmirins.folk.data.api.models.HolidaysRequest
import lv.luhmirins.folk.data.api.models.HolidaysResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface HolidaysApi {

    @POST("api/holidays")
    suspend fun getHolidays(
        @Body body: HolidaysRequest
    ): HolidaysResponse
}

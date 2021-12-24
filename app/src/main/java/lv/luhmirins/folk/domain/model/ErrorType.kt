package lv.luhmirins.folk.domain.model

enum class ErrorType(val msg: String) {
    InvalidDate("Provided incorrect date"),
    InvalidApiKey("API key is not valid"),
    InvalidRequest("Request range is not valid"),
    Unknown("Unknown error");

    companion object {
        fun fromString(error: String?) = when (error) {
            "invalid-dates" -> InvalidDate
            "invalid-api-key" -> InvalidApiKey
            "invalid-request" -> InvalidRequest
            else -> Unknown
        }
    }
}

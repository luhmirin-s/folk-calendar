package lv.luhmirins.folk.domain.model

enum class ErrorType {
    InvalidDate,
    InvalidApiKey,
    InvalidRequest,
    Unknown;

    companion object {
        fun fromString(error: String?) = when (error) {
            "invalid-dates" -> InvalidDate
            "invalid-api-key" -> InvalidApiKey
            "invalid-request" -> InvalidRequest
            else -> Unknown
        }
    }
}
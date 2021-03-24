package eu.agribot.agribotapp.domain.model.todomain.waetherresponsemodels

data class WeatherResponseData(

    var lat: Float,

    var lng: Float,

    var timezone: String?,

    var timezoneOffset: Int,

    var hourly: List<HourlyItem>?

)

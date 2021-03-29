package eu.agribot.agribotapp.domain.model.todomain.waetherresponsemodels

data class HourlyItem(

    val dt: Int,

    val temp: Double,

    val feelsLike: Double,

    val pressure: Int,

    val humidity: Int,

    val dewPoint: Double,

    val uvi: Double,

    val clouds: Int,

    val visibility: Int,

    val windSpeed: Double,

    val windDeg: Int,

    val weather: List<WeatherItem?>?,

    val pop: Double
)

package eu.agribot.agribotapp.communication.restapi.models

import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.todomain.waetherresponsemodels.HourlyItem

data class WeatherResponseHourlyItem(

    @SerializedName("dt")
    val dt: Int = 0,

    @SerializedName("temp")
    val temp: Double = 0.0,

    @SerializedName("feels_like")
    val feelsLike: Double = 0.0,

    @SerializedName("pressure")
    val pressure: Int = 0,

    @SerializedName("humidity")
    val humidity: Int = 0,

    @SerializedName("dew_point")
    val dewPoint: Double = 0.0,

    @SerializedName("uvi")
    val uvi: Double = 0.0,

    @SerializedName("clouds")
    val clouds: Int = 0,

    @SerializedName("visibility")
    val visibility: Int = 0,

    @SerializedName("wind_speed")
    val windSpeed: Double = 0.0,

    @SerializedName("wind_deg")
    val windDeg: Int = 0,

    @SerializedName("weather")
    val weather: ArrayList<WeatherResponseWeatherItem?>? = null,

    @SerializedName("pop")
    val pop: Double = 0.0
) : BaseCloudDatabaseModel<HourlyItem> {

    override fun toDomainModel() = HourlyItem(
        dt = this.dt,
        temp = this.temp,
        feelsLike = this.feelsLike,
        pressure = this.pressure,
        humidity = this.humidity,
        dewPoint = this.dewPoint,
        uvi = this.uvi,
        clouds = this.clouds,
        visibility = this.visibility,
        windSpeed = this.windSpeed,
        windDeg = this.windDeg,
        weather = this.weather?.map { it?.toDomainModel() },
        pop = this.pop
    )
}

package eu.agribot.agribotapp.communication.restapi

import com.google.gson.annotations.SerializedName

class WeatherResponse {

    @SerializedName("lat")
    var lat: Float = 0.toFloat()

    @SerializedName("lon")
    var lon: Float = 0.toFloat()

    @SerializedName("timezone")
    var timezone: String? = null

    @SerializedName("timezone_offset")
    var timezoneOffset: Int = 0


    class Hourly {
        @SerializedName("dt")
        var dt: Int = 0

        @SerializedName("temp")
        var temp: Double? = 0.0

        @SerializedName("feels_like")
        var feelsLike: Double? = 0.0

        @SerializedName("pressure")
        var pressure: Int = 0

        @SerializedName("humidity")
        var humidity: Int = 0

        @SerializedName("dew_point")
        var dewPoint: Double? = 0.0

        @SerializedName("uvi")
        var uvi: Int = 0

        @SerializedName("clouds")
        var clouds: Int = 0

        @SerializedName("visibility")
        var visibility: Int = 0

        @SerializedName("wind_speed")
        var windSpeed: Double? = 0.0

        @SerializedName("wind_deg")
        var windDeg: Int = 0

        @SerializedName("weather")
        var weather: Weather? = null

        @SerializedName("pop")
        var pop: Int = 0
    }


    class Weather {
        @SerializedName("id")
        var id: Int = 0

        @SerializedName("main")
        var main: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("icon")
        var icon: String? = null
    }


}